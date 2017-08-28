package com.sunny.youyun.internet.download;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.App;
import com.sunny.youyun.R;
import com.sunny.youyun.internet.event.FileDownloadEvent;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.utils.MyNotifyUtil;
import com.sunny.youyun.utils.Tool;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

import static com.sunny.youyun.model.InternetFile.Status.DOWNLOADING;

/**
 * Created by Sunny on 2017/8/22 0022.
 */

public class FileDownloadService extends Service {

    public static final String PARAM_URL = "url";
    public static final String PARAM_SAVE_NAME = "save name";
    public static final String PARAM_POSITION = "position";

    public static final String ACTION_DOWNLOAD = "download";
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_CONTINUE = "continue";

    private String desDir = FileUtils.getDownloadPath();

    private final Map<String, Disposable> disposableMap = new ConcurrentHashMap<>();

    private final Context mContext = this;
    private final List<InternetFile> mList = App.mList_DownloadRecord;

    @Override
    public void onCreate() {
        System.out.println("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");
        if (intent != null) {
            String url = intent.getStringExtra(PARAM_URL);
            String action = intent.getAction();
            String fileName = intent.getStringExtra(PARAM_SAVE_NAME);
            int position = intent.getIntExtra(PARAM_POSITION, 0);
            download(url, fileName, action, position);
        } else {
            System.out.println("intent is null");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void download(String url, String name, String action, int position) {
        if(position >= mList.size())
            return;
        InternetFile internetFile = mList.get(position);
        internetFile.setFileTAG(InternetFile.TAG_DOWNLOAD);
        if (url == null || url.equals("") ||
                name == null || name.equals(""))
            return;
        switch (action) {
            case ACTION_DOWNLOAD:
            case ACTION_CONTINUE:
                RxDownload.getInstance(this)
                        .download(url, name, desDir)
                        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new Observer<DownloadStatus>() {
                            long lastBytes = 0;
                            long startTime = System.currentTimeMillis();
                            long lastTime = startTime;

                            @Override
                            public void onSubscribe(Disposable d) {
                                disposableMap.put(url, d);
                                internetFile.setStatus(DOWNLOADING);
                                //此处将下载进度放到bus上
                                EventBus.getDefault()
                                        .post(new FileDownloadEvent.Builder()
                                                .type(FileDownloadEvent.Type.START)
                                                .position(position).build());
                            }

                            @Override
                            public void onNext(DownloadStatus downloadStatus) {
                                long span = System.currentTimeMillis() - lastTime;
                                if (span < 200 && (downloadStatus.getPercentNumber() != 100))
                                    return;
                                lastTime = span + lastTime;
                                String rate = Tool.convertToRate(downloadStatus.getDownloadSize() - lastBytes, span);
                                lastBytes = downloadStatus.getDownloadSize();
                                if (downloadStatus.getPercentNumber() == 100) {     //结束后发送结束标识
                                    internetFile.setStatus(InternetFile.Status.FINISH);
                                    //此处将下载进度放到bus上
                                    EventBus.getDefault()
                                            .post(new FileDownloadEvent.Builder()
//                                                    .rate(Tool.convertToRate(lastBytes, System.currentTimeMillis() - startTime))
                                                    .type(FileDownloadEvent.Type.FINISH)
                                                    .position(position)
                                                    .build());
                                } else {
                                    internetFile.setStatus(DOWNLOADING);
                                    internetFile.setProgress((int) downloadStatus.getPercentNumber());
                                    internetFile.setRate(rate);
                                    //此处将下载进度放到bus上
                                    EventBus.getDefault()
                                            .post(new FileDownloadEvent.Builder()
//                                                    .already(downloadStatus.getDownloadSize())
//                                                    .total(downloadStatus.getTotalSize())
//                                                    .percent((int) downloadStatus.getPercentNumber())
//                                                    .rate(rate)
//                                                    .done(downloadStatus.getPercentNumber() == 100)
                                                    .type(FileDownloadEvent.Type.PROGRESS)
                                                    .position(position)
                                                    .build());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                internetFile.setStatus(InternetFile.Status.ERROR);
                                internetFile.setRate(getString(R.string.trans_error));
                                EventBus.getDefault()
                                        .post(new FileDownloadEvent.Builder()
                                                .type(FileDownloadEvent.Type.ERROR)
                                                .position(position)
                                                .build());
                                showError(name);
                            }

                            @Override
                            public void onComplete() {
                                internetFile.setStatus(InternetFile.Status.FINISH);
                                //此处将下载进度放到bus上
                                EventBus.getDefault()
                                        .post(new FileDownloadEvent.Builder()
//                                                .rate(Tool.convertToRate(lastBytes, System.currentTimeMillis() - startTime))
                                                .type(FileDownloadEvent.Type.FINISH)
                                                .position(position)
                                                .build());
                                showSuccess(name);
                                dispose(url);
                                save(internetFile);
                            }
                        });
                break;
            case ACTION_PAUSE:
                internetFile.setStatus(InternetFile.Status.PAUSE);
                internetFile.setRate(getString(R.string.already_pause));
                if (dispose(url)) {
                    EventBus.getDefault()
                            .post(new FileDownloadEvent.Builder()
                                    .type(FileDownloadEvent.Type.PAUSE)
                                    .position(position)
                                    .build());
                }
                break;

        }

    }

    private void save(InternetFile internetFile) {
        boolean b = internetFile.saveOrUpdate("identifyCode=? and fileTAG=?", internetFile.getIdentifyCode(),
                String.valueOf(InternetFile.TAG_DOWNLOAD));
        Logger.i("保存：" + b);
    }

    private boolean dispose(String url) {
        Disposable disposable = disposableMap.get(url);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            return true;
        }
        return false;
    }

    private void showError(String name) {
        MyNotifyUtil.showNotify(mContext, name, getString(R.string.file_download_error), getString(R.string.file_download_error));
    }

    private void showSuccess(String fileName) {
        MyNotifyUtil.showNotify(mContext, fileName, getString(R.string.file_download_success), getString(R.string.file_download_success));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    private void saveFile(ResponseBody responseBody, String destFileDir, String destFileName) {
        InputStream is = null;
        byte[] buf = new byte[24 * 1024];
        int len;
        FileOutputStream fos = null;
        try {
            is = responseBody.byteStream();
            File dir = new File(destFileDir);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(destFileDir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
