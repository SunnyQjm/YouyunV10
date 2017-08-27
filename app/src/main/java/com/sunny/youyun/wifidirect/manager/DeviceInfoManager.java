package com.sunny.youyun.wifidirect.manager;

import com.sunny.youyun.wifidirect.model.DeviceInfo;

/**
 * This enum class support a singleton manager class
 *
 * Created by Sunny on 2017/8/1 0001.
 */

public enum DeviceInfoManager {
    INSTANCE;

    public static DeviceInfoManager getInstance(){
        return INSTANCE;
    }

    private boolean isGroupOwner;
    private String groupOwnerIp;
    private String groupOwnerMacAddr;
    private String groupOwnerName;
    private DeviceInfo deviceInfo;


    public String getGroupOwnerName() {
        return groupOwnerName;
    }

    public void setGroupOwnerName(String groupOwnerName) {
        this.groupOwnerName = groupOwnerName;
    }

    public boolean isGroupOwner() {
        return isGroupOwner;
    }

    public void setGroupOwner(boolean groupOwner) {
        isGroupOwner = groupOwner;
    }

    public String getGroupOwnerIp() {
        return groupOwnerIp;
    }

    public void setGroupOwnerIp(String groupOwnerIp) {
        this.groupOwnerIp = groupOwnerIp;
    }

    public String getGroupOwnerMacAddr() {
        return groupOwnerMacAddr;
    }

    public void setGroupOwnerMacAddr(String groupOwnerMacAddr) {
        this.groupOwnerMacAddr = groupOwnerMacAddr;
    }

    public DeviceInfo getDeviceInfo() {
        if(deviceInfo == null)
            deviceInfo = new DeviceInfo.Builder().build();
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
