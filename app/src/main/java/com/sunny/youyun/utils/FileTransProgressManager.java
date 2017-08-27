package com.sunny.youyun.utils;

import android.util.Log;

/**
 * Created by Sunny on 2017/6/24 0024.
 */

public enum  FileTransProgressManager {
    INSTANCE;
    private volatile OnUploadListener onUploadListener;
    private volatile OnDownloadListener onDownloadListener;

    public static FileTransProgressManager getInstance() {
        return INSTANCE;
    }

    public interface OnUploadListener {
        void onUploadProgress(long alreadyTrans, long totalLength, boolean finish, int position);
    }

    public interface OnDownloadListener {
        void onDownloadProgress(long alreadyTrans, long totalLength, boolean finish, int position);
    }


    public OnUploadListener getOnUploadListener() {
        if (onUploadListener == null) {
            onUploadListener = (alreadyTrans, totalLength, finish, position) -> System.out.println("默认上传文件回调");
        }
        return onUploadListener;
    }

    public OnDownloadListener getOnDownloadListener() {
        if (onDownloadListener == null)
            onDownloadListener = (alreadyTrans, totalLength, finish, position) -> System.out.println("默认下载文件回调");
        return onDownloadListener;
    }

    public static void bindUploadListener(OnUploadListener listener) {
        if(listener == getInstance().onUploadListener){     //如果视图绑定同一个监听器，就不做处理
            return;
        }
        Log.e("FileTransManager", "Bind");
        if (listener != null){
            getInstance().onUploadListener = listener;
            System.out.println("修改默认回调");
        }else{
            Log.e("FileTransManager", "listener is null");
        }

    }

    public static void bindDownloadListener(OnDownloadListener listener) {
        if(listener == getInstance().onDownloadListener)
            return;
        if (listener != null)
            getInstance().onDownloadListener = listener;
    }

}
