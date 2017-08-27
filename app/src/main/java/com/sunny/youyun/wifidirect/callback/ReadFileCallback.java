package com.sunny.youyun.wifidirect.callback;


import com.sunny.youyun.wifidirect.model.TransLocalFile;

/**
 * Created by Sunny on 2017/5/7 0007.
 */
public interface ReadFileCallback {
    void onBegin(int position);
    void onProcess(TransLocalFile transLocalFile, int process, int position);
    void onEnd(TransLocalFile localFile, int position);
    void onError(TransLocalFile localFile, int position);
}
