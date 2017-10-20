package com.sunny.youyun.internet.download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.sunny.youyun.App;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.FileUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.Disposable;
import zlc.season.rxdownload3.core.Mission;

/**
 * Created by Sunny on 2017/8/22 0022.
 */

public enum FileDownloader {
    @SuppressLint("StaticFieldLeak")INSTANCE;

    public static FileDownloader getInstance() {
        return INSTANCE;
    }

    private Context context = null;
    private String desDir = FileUtils.getDownloadPath();

    private final Map<String, Disposable> disposableMap = new ConcurrentHashMap<>();
    private final Map<String, Mission> missionMap = new ConcurrentHashMap<>();

    private final List<InternetFile> mList = App.mList_DownloadRecord;

    public static void bind(Context context) {
        getInstance().context = context;
    }

    public static void unBind() {
        getInstance().context = null;
    }

    public void download(String url, String fileName, int position) {
        if (context == null)
            return;
        action2Service(url, fileName, FileDownloadService.ACTION_DOWNLOAD, position);
    }

    public void pause(String url, int position) {
        if (context == null)
            return;
        action2Service(url, "pause", FileDownloadService.ACTION_PAUSE, position);
    }

    public void cancel(String url, int position){
        if(context == null)
            return;
        action2Service(url, "cancel", FileDownloadService.ACTION_CANCEL, position);
    }

    public void continueDownload(String url, int position) {
        if(context == null)
            return;
        action2Service(url, "continue", FileDownloadService.ACTION_CONTINUE, position);
    }

    private void action2Service(String url, String fileName, String action, int position) {
        Intent intent = new Intent(context, FileDownloadService.class);
        intent.setAction(action);
        intent.putExtra(FileDownloadService.PARAM_SAVE_NAME, fileName);
        intent.putExtra(FileDownloadService.PARAM_URL, url);
        intent.putExtra(FileDownloadService.PARAM_POSITION, position);
        context.startService(intent);
    }
}
