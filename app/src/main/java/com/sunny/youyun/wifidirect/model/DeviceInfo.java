package com.sunny.youyun.wifidirect.model;

/**
 * Created by Sunny on 2017/8/1 0001.
 */

public class DeviceInfo {
    private String deviceName;
    private String ip;
    private int listenPort;     //一个节点选择监听的端口
    private String macAddress;
    private long expire;

    private DeviceInfo(Builder builder) {
        setDeviceName(builder.deviceName);
        setIp(builder.ip);
        setListenPort(builder.listenPort);
        setMacAddress(builder.macAddress);
        expire = builder.expipre;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getIp() {
        return ip;
    }

    public int getListenPort() {
        return listenPort;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public long getExpire() {
        return expire;
    }

    public boolean isEffective(){
        return expire > System.currentTimeMillis();
    }
    @Override
    public String toString() {
        return "DeviceInfo{" +
                "deviceName='" + deviceName + '\'' +
                ", ip='" + ip + '\'' +
                ", listenPort=" + listenPort +
                ", macAddress='" + macAddress + '\'' +
                '}';
    }

    public static final class Builder {
        private String ip;
        private int listenPort;
        private String macAddress;
        private long expipre;
        private String macAddr;
        private String deviceName;

        public Builder() {
        }


        public Builder ip(String val) {
            ip = val;
            return this;
        }

        public Builder listenPort(int val) {
            listenPort = val;
            return this;
        }

        public Builder macAddress(String val) {
            macAddress = val;
            return this;
        }

        public Builder expipre(long val) {
            expipre = val;
            return this;
        }

        public Builder macAddr(String val) {
            macAddr = val;
            return this;
        }

        public DeviceInfo build() {
            return new DeviceInfo(this);
        }

        public Builder deviceName(String val) {
            deviceName = val;
            return this;
        }
    }
}
