package com.sunny.youyun.wifidirect.callback;


import com.sunny.youyun.wifidirect.model.TransLocalFile;

/**
 * Created by Sunny on 2017/5/14 0014.
 */

public interface SendFileCallBack {
    void onBegin(int position);
    void onProcess(TransLocalFile transLocalFile, int process, int position);
    void onEnd(TransLocalFile localFile, int position);
    void onError(TransLocalFile localFile, int position);
}
