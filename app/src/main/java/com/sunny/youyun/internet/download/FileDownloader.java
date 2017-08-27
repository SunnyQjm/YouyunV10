package com.sunny.youyun.internet.download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Sunny on 2017/8/22 0022.
 */

public enum FileDownloader {
    @SuppressLint("StaticFieldLeak")INSTANCE;

    public static FileDownloader getInstance() {
        return INSTANCE;
    }

    private Context context = null;

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
