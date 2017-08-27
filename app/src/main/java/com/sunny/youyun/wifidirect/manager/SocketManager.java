package com.sunny.youyun.wifidirect.manager;

import com.sunny.youyun.wifidirect.client.ForwardClientStrategy;
import com.sunny.youyun.wifidirect.model.DeviceInfo;
import com.sunny.youyun.wifidirect.model.TransLocalFile;
import com.sunny.youyun.wifidirect.server.ServerSocketStrategy;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/7/31 0031.
 */

interface SocketManager {
    /**
     * 开始Socket监听
     */
    void startServer(ServerSocketStrategy strategy);

    void beginDeviceInfoChange(int port, DeviceInfo deviceInfo) throws IOException;

    void stopDeviceInfoChange();

    /**
     * 停止Socket监听
     *
     * @throws IOException Socket关闭异常时抛出
     */
    void stopServer() throws IOException;

    void sendFile(String path, String IP, List<TransLocalFile> mList);

    void askIp(String IP) throws IOException;

    void beginReceiveDeviceInfo(int port);

    void beginSendDeviceInfo(int port, DeviceInfo deviceInfo, ForwardClientStrategy strategy) throws IOException;

    void beginSendDeviceInfo(int port, DeviceInfo deviceInfo) throws IOException;

    void stopReceiveDeviceInfo();

    void stopSendDeviceInfo();

    void beginDeviceInfoChange(int port, DeviceInfo deviceInfo, ForwardClientStrategy strategy) throws IOException;
}
