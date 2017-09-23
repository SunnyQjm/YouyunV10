package com.sunny.youyun.wifidirect.youyun_easy_chage;

import com.sunny.youyun.wifidirect.config.SocketConfig;
import com.sunny.youyun.wifidirect.server.ServerSocketImpl;
import com.sunny.youyun.wifidirect.server.ServerSocketStrategy;
import com.sunny.youyun.wifidirect.server.SingleTransMode;

import java.io.IOException;

/**
 * Created by Sunny on 2017/9/23 0023.
 */

public enum  YouyunEasyChangeServer {
    INSTANCE;
    public static YouyunEasyChangeServer getInstance(){
        return INSTANCE;
    }

    private ServerSocketImpl serverSocket = null;
    private int port = SocketConfig.FileListenPort;
    private ServerSocketStrategy serverSocketStrategy = null;

    public YouyunEasyChangeServer registerStrategy(ServerSocketStrategy serverSocketStrategy){
        this.serverSocketStrategy = serverSocketStrategy;
        return this;
    }

    public YouyunEasyChangeServer port(int port){
        this.port = port;
        return this;
    }

    public void start() throws IOException {
        if(serverSocketStrategy == null) {
            serverSocketStrategy = SingleTransMode.getInstance();
        }
        serverSocket = new ServerSocketImpl.Builder()
                .strategy(serverSocketStrategy)
                .port(port)
                .build();
    }

//    public void test(){
//        YouyunEasyChangeServer.getInstance()
//                .registerStrategy()
//                .setPort()
//                .start();
//    }
}
