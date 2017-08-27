package com.sunny.youyun.wifidirect.client;

import com.sunny.youyun.wifidirect.config.SocketConfig;
import com.sunny.youyun.wifidirect.model.DeviceInfo;
import com.sunny.youyun.wifidirect.utils.MyThreadPool;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class main {
    public static void main(String[] args) throws IOException, InterruptedException {
        DeviceInfo deviceInfo = new DeviceInfo.Builder()
                .deviceName("ds")
                .ip("192.1168.1.2")
                .listenPort(3000)
                .macAddress("asdfasdfasf")
                .build();
        ForwardClient client = new ForwardClient(new ForwardClientStrategyImpl());
        client.broadcastDeviceInfo(SocketConfig.FORWARD_ITEM_LISTEN_PORT, deviceInfo);
        TimeUnit.SECONDS.sleep(10);
        client.stop();
        MyThreadPool.getInstance().shutDown();
    }
}
