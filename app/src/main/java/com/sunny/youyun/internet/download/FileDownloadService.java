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
import com.sunny.youyun.utils.Tool;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload3.RxDownload;
import zlc.season.rxdownload3.core.Downloading;
import zlc.season.rxdownload3.core.Failed;
import zlc.season.rxdownload3.core.Mission;
import zlc.season.rxdownload3.core.Normal;
import zlc.season.rxdownload3.core.Status;
import zlc.season.rxdownload3.core.Succeed;
import zlc.season.rxdownload3.core.Suspend;
import zlc.season.rxdownload3.core.Waiting;
import zlc.season.rxdownload3.extension.ApkInstallExtension;

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
    public static final String ACTION_CANCEL = "cancel";

    private String desDir = FileUtils.getDownloadPath();

    private final Map<String, Disposable> disposableMap = new ConcurrentHashMap<>();
    private final Map<String, Mission> missionMap = new ConcurrentHashMap<>();

    private final List<InternetFile> mList = App.mList_DownloadRecord;
    private final Context mContext = this;

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
            download(url, action, position);
        } else {
            System.out.println("intent is null");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void download(String url, String action, int position) {
        if (position >= mList.size())
            return;
        InternetFile internetFile = mList.get(position);
        internetFile.setFileTAG(InternetFile.TAG_DOWNLOAD);
        String name = internetFile.getName();
        if (url == null || url.equals("") ||
                name == null || name.equals(""))
            return;

        switch (action) {
            case ACTION_DOWNLOAD:
//                while (new File(desDir + name).exists()) {
//                    name = FileTypeUtil.reName(name);
//                }
//                if (!name.equals(""))
//                    internetFile.setName(name);
            case ACTION_CONTINUE:
                internetFile.setStatus(DOWNLOADING);
                //此处将下载进度放到bus上
                EventBus.getDefault()
                        .post(new FileDownloadEvent.Builder()
                                .type(FileDownloadEvent.Type.START)
                                .position(position).build());
                Mission mission = null;
                if (missionMap.get(url) == null)      //第一次先创建Mission
                    mission = create(url, internetFile, position);
                else {
                    mission = missionMap.get(url);
                    start(mission, internetFile, position);
                }
                save(internetFile);
                break;
            case ACTION_PAUSE:
                internetFile.setStatus(InternetFile.Status.PAUSE);
                internetFile.setRate(getString(R.string.already_pause));
                EventBus.getDefault()
                        .post(new FileDownloadEvent.Builder()
                                .type(FileDownloadEvent.Type.PAUSE)
                                .position(position)
                                .build());
                mission = missionMap.get(url);
                if(mission != null)
                    stop(mission);
                break;
            case ACTION_CANCEL:
                internetFile.setStatus(InternetFile.Status.CANCEL);
                internetFile.setStatus(getString(R.string.already_cancel));
                mission = missionMap.get(url);
                if (mission != null) {
                    RxDownload.INSTANCE
                            .delete(mission, true);
                }
                if (dispose(url)) {
                    EventBus.getDefault()
                            .post(new FileDownloadEvent.Builder()
                                    .type(FileDownloadEvent.Type.ERROR)
                                    .position(position)
                                    .build());
                }
                break;
        }

    }

    private void start(Mission mission, InternetFile internetFile, int position) {
        RxDownload.INSTANCE
                .start(mission)
                .subscribe(new MaybeObserver<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Object o) {
                        System.out.println("onSuccess");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Logger.e("下载失败", e);
                        internetFile.setStatus(InternetFile.Status.ERROR);
                        internetFile.setRate(getString(R.string.trans_error));
                        EventBus.getDefault()
                                .post(new FileDownloadEvent.Builder()
                                        .type(FileDownloadEvent.Type.ERROR)
                                        .position(position)
                                        .build());
                        save(internetFile);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    private void stop(Mission mission) {
        RxDownload.INSTANCE.stop(mission).subscribe();
    }

    private Mission create(String url, InternetFile internetFile, int position) {
        Mission mission = new Mission(url, internetFile.getName(), desDir);
        missionMap.put(url, mission);
        Disposable d = RxDownload.INSTANCE
                .create(mission)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Status>() {
                    long lastBytes = 0;
                    long startTime = System.currentTimeMillis();
                    long lastTime = startTime;

                    @Override
                    public void accept(Status status) throws Exception {//onNext
                        //创建成功后开始下载
                        if (status instanceof Normal)
                            start(mission, internetFile, position);
                        long span = System.currentTimeMillis() - lastTime;
                        dealStatus(internetFile
                                , lastBytes, span, position, status);
                        if(span < 200)
                            return;
                        lastTime = span + lastTime;
                        lastBytes = status.getDownloadSize();
                    }
                }, t -> {   //onError
                    t.printStackTrace();
                    Logger.e("下载失败", t);
                    internetFile.setStatus(InternetFile.Status.ERROR);
                    internetFile.setRate(getString(R.string.trans_error));
                    EventBus.getDefault()
                            .post(new FileDownloadEvent.Builder()
                                    .type(FileDownloadEvent.Type.ERROR)
                                    .position(position)
                                    .build());
                    save(internetFile);
                }, () -> {      //onComplete
                    internetFile.setStatus(InternetFile.Status.FINISH);
                    //此处将下载进度放到bus上
                    EventBus.getDefault()
                            .post(new FileDownloadEvent.Builder()
                                    .type(FileDownloadEvent.Type.FINISH)
                                    .position(position)
                                    .build());
                    dispose(url);
                    save(internetFile);
                });
        disposableMap.put(url, d);
        return mission;
    }

    private void dealStatus(InternetFile internetFile, long lastBytes, long span, int position, Status status) {
        System.out.println("status: " + status);
        if (status instanceof Normal) {
            internetFile.setStatus(InternetFile.Status.DOWNLOADING);
            dispatchEvent(FileDownloadEvent.Type.START);
        } else if (status instanceof Suspend) {
            internetFile.setStatus(InternetFile.Status.PAUSE);
            dispatchEvent(FileDownloadEvent.Type.PAUSE);
        } else if (status instanceof Waiting) {
            internetFile.setStatus(InternetFile.Status.DOWNLOADING);
            internetFile.setRate("等待下载");
            dispatchEvent(FileDownloadEvent.Type.START);
        } else if (status instanceof Downloading) {
            if(span < 200)
                return;
            downloading(internetFile, lastBytes, span, position, status);
        } else if (status instanceof Failed) {
            internetFile.setStatus(InternetFile.Status.ERROR);
            dispatchEvent(FileDownloadEvent.Type.ERROR);
        } else if (status instanceof Succeed) {
            internetFile.setStatus(InternetFile.Status.FINISH);
            //此处将下载进度放到bus上
            EventBus.getDefault()
                    .post(new FileDownloadEvent.Builder()
                            .type(FileDownloadEvent.Type.FINISH)
                            .position(position)
                            .build());
            save(internetFile);
        } else if (status instanceof ApkInstallExtension.Installing) {
        } else if (status instanceof ApkInstallExtension.Installed) {
        }
    }

    private void dispatchEvent(FileDownloadEvent.Type type) {
        EventBus.getDefault()
                .post(new FileDownloadEvent.Builder()
                        .type(type)
                        .build());
    }

    private void downloading(InternetFile internetFile, long lastBytes, long span,
                             int position, Status status) {
        String rate = Tool.convertToRate(status.getDownloadSize() - lastBytes, span);
        int percent = getPercent(status);
        if (percent == 100) {     //结束后发送结束标识
            internetFile.setStatus(InternetFile.Status.FINISH);
            //此处将下载进度放到bus上
            EventBus.getDefault()
                    .post(new FileDownloadEvent.Builder()
                            .type(FileDownloadEvent.Type.FINISH)
                            .position(position)
                            .build());
        } else {
            internetFile.setStatus(DOWNLOADING);
            internetFile.setProgress(percent);
            internetFile.setRate(rate);
            //此处将下载进度放到bus上
            EventBus.getDefault()
                    .post(new FileDownloadEvent.Builder()
                            .type(FileDownloadEvent.Type.PROGRESS)
                            .position(position)
                            .build());
        }
    }

    private void save(InternetFile internetFile) {
        boolean b = internetFile.saveOrUpdate("identifyCode=? and fileTAG=? and createTime=?",
                internetFile.getIdentifyCode(), String.valueOf(InternetFile.TAG_DOWNLOAD),
                String.valueOf(internetFile.getCreateTime()));
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

    private int getPercent(Status status) {
        if (status == null || status.getTotalSize() == 0)      //避免除零错误
            return 0;
        return (int) (status.getDownloadSize() * 100 / status.getTotalSize());
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
}
