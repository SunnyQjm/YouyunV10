package com.sunny.youyun.wifidirect.client;

import com.sunny.youyun.wifidirect.model.DeviceInfo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

class ForwardClientRunnable implements Runnable {
    private final ForwardClientStrategy strategy;
    private final int port;
    private final DeviceInfo deviceInfo;

    private volatile boolean isStop = false;

    private ForwardClientRunnable(Builder builder) {
        strategy = builder.strategy;
        port = builder.port;
        deviceInfo = builder.deviceInfo;
        isStop = builder.isStop;
    }

    private boolean isStop() {
        return isStop;
    }



    @Override
    public void run() {
        while (!isStop()){
            try {
                strategy.broadcastDeviceInfo(port, deviceInfo);
                TimeUnit.SECONDS.sleep(1);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            strategy.deleteDeviceInfo(port, deviceInfo);
            strategy.deleteDeviceInfo(port, deviceInfo);
            strategy.deleteDeviceInfo(port, deviceInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean stop(){
        isStop = true;
        return true;
    }

    public static final class Builder {
        private ForwardClientStrategy strategy;
        private int port;
        private DeviceInfo deviceInfo;
        private boolean isStop;

        public Builder() {
        }

        public Builder strategy(ForwardClientStrategy val) {
            strategy = val;
            return this;
        }

        public Builder port(int val) {
            port = val;
            return this;
        }

        public Builder deviceInfo(DeviceInfo val) {
            deviceInfo = val;
            return this;
        }

        public Builder isStop(boolean val) {
            isStop = val;
            return this;
        }

        public ForwardClientRunnable build() {
            return new ForwardClientRunnable(this);
        }
    }
}
