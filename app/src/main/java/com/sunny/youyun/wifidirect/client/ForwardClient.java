package com.sunny.youyun.wifidirect.client;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.wifidirect.model.DeviceInfo;
import com.sunny.youyun.wifidirect.utils.MyThreadPool;

import java.io.IOException;

public class ForwardClient implements ForwardClientStrategy{
    private final ForwardClientStrategy strategy;
    private ForwardClientRunnable forwardClientRunnable = null;
    private boolean isStop = true;

    public ForwardClient(ForwardClientStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void broadcastDeviceInfo(int port, DeviceInfo deviceInfo) throws IOException {
        isStop = false;
        Logger.i("开始发送心跳包");
        forwardClientRunnable = new ForwardClientRunnable.Builder()
                .strategy(strategy)
                .deviceInfo(deviceInfo)
                .port(port)
                .build();
        MyThreadPool.getInstance()
                .submit(forwardClientRunnable);
    }

    @Override
    public void deleteDeviceInfo(int port, DeviceInfo deviceInfo) throws IOException {
        isStop = false;
        forwardClientRunnable = new ForwardClientRunnable.Builder()
                .strategy(strategy)
                .deviceInfo(deviceInfo)
                .port(port)
                .isStop(true)           //该项设置为true的话，就不会执行发送表项操作，直接执行三次删除操作
                .build();
        MyThreadPool.getInstance()
                .submit(forwardClientRunnable);
    }


    public boolean isStop() {
        return isStop;
    }

    public boolean stop() {
        isStop = true;
        return forwardClientRunnable != null && forwardClientRunnable.stop();
    }
}
