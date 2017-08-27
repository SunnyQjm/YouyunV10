package com.sunny.youyun.wifidirect.client;

import com.sunny.youyun.wifidirect.model.DeviceInfo;

import java.io.IOException;

public interface ForwardClientStrategy {

    /**
     * This method broadcast current device's info to all device belong to this Network
     * @param port
     * @param deviceInfo
     */
    void broadcastDeviceInfo(int port, DeviceInfo deviceInfo) throws IOException;

    void deleteDeviceInfo(int port, DeviceInfo deviceInfo) throws IOException;
}
