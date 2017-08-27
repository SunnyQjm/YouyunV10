package com.sunny.youyun.activity.scan;

/**
 * Created by Sunny on 2017/8/11 0011.
 */

class ScanPresenter extends ScanContract.Presenter{
    ScanPresenter(ScanActivity scanActivity) {
        mView = scanActivity;
        mModel = new ScanModel(this);
    }

    @Override
    protected void start() {

    }
}
