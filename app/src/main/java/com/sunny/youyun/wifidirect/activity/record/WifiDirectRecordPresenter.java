package com.sunny.youyun.wifidirect.activity.record;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

class WifiDirectRecordPresenter extends WifiDirectRecordContract.Presenter{
    WifiDirectRecordPresenter(WifiDirectRecordActivity wifiDirectRecordActivity) {
        mView = wifiDirectRecordActivity;
        mModel = new WifiDirectRecordModel(this);
    }

    @Override
    protected void start() throws IOException {

    }
}
