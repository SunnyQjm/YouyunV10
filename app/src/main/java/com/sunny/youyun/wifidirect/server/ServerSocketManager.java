package com.sunny.youyun.wifidirect.server;

import com.sunny.youyun.wifidirect.base.BaseRunnable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class manage serverSocket
 * need a port to listen to
 * need a Strategy to service for each socket
 * Created by Sunny on 2017/4/21 0021.
 */
public class ServerSocketManager extends BaseRunnable {

    private final ServerSocket serverSocket;
    private final ServerSocketStrategy strategy;

    private ServerSocketManager(Builder builder) throws IOException {
        serverSocket = new ServerSocket(builder.port);
        strategy = builder.strategy;
    }

    public void run() {
        System.out.println("服务器启动，开始接收文件或者文字");
        ServerSocketThreadSupport serverSocketThreadSupport = new ServerSocketThreadSupport(strategy);
        while(!isStop()){
            try {
                Socket socket = serverSocket.accept();
                serverSocketThreadSupport.service(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("服务器结束！");
    }

    /**
     * This method return whether the ServerSocket is still listening
     * @return  if com.sunny.youyun.wifidirect.server is listening return true, or return false
     */
    public boolean isAlive(){
        return !serverSocket.isClosed();
    }

    @Override
    public void stop() throws IOException {
        isStop = true;
        if(!serverSocket.isClosed()){       //server不为空，且未关闭，则关闭它
            serverSocket.close();
        }
    }

    public static final class Builder {
        private int port;
        private ServerSocketStrategy strategy;

        public Builder() {
        }

        public Builder port(int val){
            this.port = val;
            return this;
        }

        public Builder strategy(ServerSocketStrategy val) {
            strategy = val;
            return this;
        }

        public ServerSocketManager build() throws IOException {
            return new ServerSocketManager(this);
        }
    }
}
