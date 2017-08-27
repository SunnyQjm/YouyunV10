package com.sunny.youyun.wifidirect.server;

import com.sunny.youyun.wifidirect.utils.MyThreadPool;

import java.net.Socket;

/**
 * This class provide a Thread support for ServerSocket.
 * In order to run each Socket deal task in different thread
 */
class ServerSocketThreadSupport implements ServerSocketStrategy {

    private final ServerSocketStrategy strategy;

    ServerSocketThreadSupport(ServerSocketStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void service(Socket socket) {
        MyThreadPool.getInstance()
                .submit(() -> strategy.service(socket));
    }
}
