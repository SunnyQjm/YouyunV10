package com.sunny.youyun.wifidirect.manager;

import com.sunny.youyun.wifidirect.model.DeviceInfo;

/**
 * Created by Sunny on 2017/8/13 0013.
 */

public enum  SingleTransManager {
    INSTANCE;
    public static SingleTransManager getInstance(){
        return INSTANCE;
    }

    private volatile DeviceInfo myInfo = new DeviceInfo.Builder().build();
    private volatile DeviceInfo targetInfo = new DeviceInfo.Builder().build();

    public DeviceInfo getMyInfo() {
        return myInfo;
    }

    public DeviceInfo getTargetInfo() {
        return targetInfo;
    }

    public void clearInfo(){
        myInfo = new DeviceInfo.Builder().build();
        targetInfo = new DeviceInfo.Builder().build();
    }
}

