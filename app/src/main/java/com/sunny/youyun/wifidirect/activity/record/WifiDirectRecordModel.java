package com.sunny.youyun.wifidirect.activity.record;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

class WifiDirectRecordModel implements WifiDirectRecordContract.Model{
    private WifiDirectRecordPresenter mPresenter;
    WifiDirectRecordModel(WifiDirectRecordPresenter wifiDirectRecordPresenter) {
        mPresenter = wifiDirectRecordPresenter;
    }
}
