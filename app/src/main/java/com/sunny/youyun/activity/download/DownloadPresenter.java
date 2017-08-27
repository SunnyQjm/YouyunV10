package com.sunny.youyun.activity.download;

import com.sunny.youyun.model.InternetFile;

/**
 * Created by Sunny on 2017/8/9 0009.
 */

class DownloadPresenter extends DownloadContract.Presenter{
    DownloadPresenter(DownloadActivity downloadActivity) {
        mView = downloadActivity;
        mModel = new DownloadModel(this);
    }

    @Override
    protected void start() {

    }

    @Override
    void getFileInfo(String code) {
        mModel.getFileInfo(code);
    }

    @Override
    void showDetail(InternetFile data) {
        mView.showDetail(data);
    }
}
