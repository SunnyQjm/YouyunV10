package com.sunny.youyun.wifidirect.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.wifidirect.client.ClientSocketManager;
import com.sunny.youyun.wifidirect.client.ForwardClient;
import com.sunny.youyun.wifidirect.client.ForwardClientStrategy;
import com.sunny.youyun.wifidirect.client.ForwardClientStrategyImpl;
import com.sunny.youyun.wifidirect.config.SocketConfig;
import com.sunny.youyun.wifidirect.model.DeviceInfo;
import com.sunny.youyun.wifidirect.model.TransLocalFile;
import com.sunny.youyun.wifidirect.server.forward.ForwardServer;
import com.sunny.youyun.wifidirect.server.ServerSocketImpl;
import com.sunny.youyun.wifidirect.server.ServerSocketStrategy;
import com.sunny.youyun.wifidirect.utils.MyThreadPool;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/7/31 0031.
 */

class SocketManagerImpl implements SocketManager {

    private Context context;
    private ServerSocketImpl startFileServerSocket = null;
    private ForwardClient forwardClient = null;
    private ForwardServer forwardServer = null;

    private SocketManagerImpl(Builder builder) {
        context = builder.context;
    }


    @Override
    public void startServer(@NonNull ServerSocketStrategy strategy) {
        //第一次或
        if (startFileServerSocket == null || !startFileServerSocket.isAlive()) {
            StartFileServer(strategy);
            System.out.println("SocketServer begin listen file and msg");
        } else {
            System.out.println("SocketServer already ");
        }
    }


    @Override
    public void beginDeviceInfoChange(int port, DeviceInfo deviceInfo, ForwardClientStrategy strategy) throws IOException {
        beginSendDeviceInfo(port, deviceInfo, strategy);
        beginReceiveDeviceInfo(port);
    }

    @Override
    public void beginDeviceInfoChange(int port, DeviceInfo deviceInfo) throws IOException {
        beginSendDeviceInfo(port, deviceInfo);
        beginReceiveDeviceInfo(port);
    }

    @Override
    public void stopDeviceInfoChange() {
        stopReceiveDeviceInfo();
        stopSendDeviceInfo();
    }


    @Override
    public void stopServer() throws IOException {
        if (startFileServerSocket.isAlive()) {
            startFileServerSocket.stop();
        }
    }

    @Override
    public void sendFile(String path, String IP, final List<TransLocalFile> mList) {
        Runnable runnable = ClientSocketManager.ThreadTaskBuilder.newSendFileTask(path, IP, SocketConfig.FileListenPort, new ClientSocketManager.ThreadTaskBuilder.ThreadTaskCallback() {
            @Override
            public void onError(String errInfo) {
                Logger.e(errInfo);
            }

            @Override
            public void onSuccess() {
                Logger.i("文件发送成功");
            }
        }, mList);
        MyThreadPool.getInstance().submit(runnable);
    }

    @Override
    public void askIp(String IP) {

    }


    @Override
    public void beginReceiveDeviceInfo(int port) {
        if (forwardServer != null && !forwardServer.isStop())
            forwardServer.stop();
        if (forwardServer == null)
            forwardServer = new ForwardServer(port);
        MyThreadPool.getInstance()
                .submit(forwardServer);
    }

    @Override
    public void beginSendDeviceInfo(int port, DeviceInfo deviceInfo, ForwardClientStrategy strategy) throws IOException {
        if (forwardClient == null)
            forwardClient = new ForwardClient(strategy);
        if (forwardClient.isStop())
            forwardClient.broadcastDeviceInfo(port, deviceInfo);
    }

    @Override
    public void beginSendDeviceInfo(int port, DeviceInfo deviceInfo) throws IOException {
        beginSendDeviceInfo(port, deviceInfo, new ForwardClientStrategyImpl());
    }

    @Override
    public void stopReceiveDeviceInfo() {
        if (!forwardServer.isStop()) {
            forwardServer.stop();
        }
    }

    @Override
    public void stopSendDeviceInfo() {
        if (!forwardClient.isStop())
            forwardClient.stop();
    }


    /**
     * 开启文件接收服务监听
     */
    private void StartFileServer(ServerSocketStrategy strategy) {
        if (startFileServerSocket != null && startFileServerSocket.isAlive())
            return;
        try {
            startFileServerSocket = new ServerSocketImpl.Builder()
                    .port(SocketConfig.FileListenPort)
                    .strategy(strategy)
                    .build();
            MyThreadPool.getInstance().submit(startFileServerSocket);
        } catch (IOException e) {
            Logger.e(e, "文件监听线程启动失败");
        }
    }

    public static final class Builder {
        private Context context;

        public Builder() {
        }

        public Builder context(Context val) {
            context = val;
            return this;
        }

        public SocketManagerImpl build() {
            return new SocketManagerImpl(this);
        }
    }
}
