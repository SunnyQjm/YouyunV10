package com.sunny.youyun.internet.upload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.App;
import com.sunny.youyun.R;
import com.sunny.youyun.internet.event.FileUploadEvent;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.MyNotifyUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.Disposable;

public class FileUploadService extends Service {
    public static final String PARAM_FILE_PATH = "file_path";
    public static final String PARAM_IS_SHARE = "is_share";
    public static final String PARAM_ALLOW_DOWN_COUNT = "allow_down_count";
    public static final String PARAM_EXPIRE_TIME = "expire_time";
    public static final String PARAM_SCORE = "score";
    public static final String PARAM_IS_PRIVATE = "is_private";
    public static final String PARAM_PARENT_ID = "parent_id";               //父路径
    public static final String PARAM_POSITION = "position";
    public static final String PARAM_DESCRIPTION = "description";

    public static final String ACTION_UPLOAD = "upload";
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_CANCEL = "cancel";
    public static final String ACTION_CONTINUE = "continue";

    private final Map<String, Disposable> disposableMap = new ConcurrentHashMap<>();
    private final Context mContext = this;
    private final List<InternetFile> mList = App.mList_UploadRecord;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Logger.i("onStartCommand upload");
            String filePath = intent.getStringExtra(PARAM_FILE_PATH);
            boolean isShare = intent.getBooleanExtra(PARAM_IS_SHARE, true);
            int allowDownloadCount = intent.getIntExtra(PARAM_ALLOW_DOWN_COUNT, -1);
            long expireTime = intent.getLongExtra(PARAM_EXPIRE_TIME, -1);
            int score = intent.getIntExtra(PARAM_SCORE, 0);
            boolean isPrivate = intent.getBooleanExtra(PARAM_IS_PRIVATE, !isShare);
            String description = intent.getStringExtra(PARAM_DESCRIPTION);
            String parentId = intent.getStringExtra(PARAM_PARENT_ID);
            String action = intent.getAction();
            int position = intent.getIntExtra(PARAM_POSITION, 0);
            try {
                upload(filePath, isShare, allowDownloadCount, expireTime, score,
                        isPrivate, parentId, action, position, description);
            } catch (IOException e) {
                Logger.e("上传文件错误", e);
                EventBus.getDefault()
                        .post(new FileUploadEvent.Builder()
                                .type(FileUploadEvent.Type.ERROR)
                                .position(position)
                                .build());
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void upload(String filePath, boolean isShare, int allowDownloadCount, long expireTime,
                        int score, boolean isPrivate, String parentId, String action,
                        int position, String description) throws IOException {
        if (position >= mList.size())
            return;
        InternetFile internetFile = mList.get(position);
        internetFile.setFileTAG(InternetFile.TAG_UPLOAD);
        switch (action) {
            case ACTION_PAUSE:
            case ACTION_CANCEL:
                internetFile.setRate("已暂停");
                internetFile.setStatus(InternetFile.Status.PAUSE);
                dispose(filePath);
                break;
            case ACTION_UPLOAD:
            case ACTION_CONTINUE:
                FileUploadFileParam uploadFileParam = new FileUploadFileParam.Builder()
                        .filePath(filePath)
                        .allowDownCount(allowDownloadCount)
                        .isPrivate(isPrivate)
                        .expireTime(expireTime)
                        .description(description)
                        .score(score)
                        .isShare(isShare)
                        .parentId(parentId)
                        .build();
                FileUploaderUtils.uploadWithCheck(internetFile, uploadFileParam, position,
                        new FileUploaderUtils.FileUploadCallback() {
                            @Override
                            public void onSuccess(InternetFile internetFile, int position) {
                                showSuccess(internetFile);
                                Logger.i("upload success");
                            }

                            @Override
                            public void onFinish(InternetFile internetFile, int position) {
                                dispose(filePath);
                                Logger.i("upload finish");
                            }

                            @Override
                            public void onSubscribe(Disposable disposable) {
                                disposableMap.put(filePath, disposable);
                            }

                            @Override
                            public void onError(InternetFile internetFile, int position) {
                                showError(internetFile);
                                Logger.e("upload err", internetFile);
                            }
                        });
                break;
        }
    }

    private void showError(InternetFile internetFile) {
        MyNotifyUtil.showNotifyExceptMain(mContext, internetFile.getName(), getString(R.string.upload_error),
                getString(R.string.upload_error));
    }

    private void showSuccess(InternetFile internetFile) {
        MyNotifyUtil.showNotifyExceptMain(mContext, internetFile.getName(), getString(R.string.upload_success),
                getString(R.string.upload_success));
    }

    private boolean dispose(String filePath) {
        Disposable disposable = disposableMap.get(filePath);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            return true;
        }
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
