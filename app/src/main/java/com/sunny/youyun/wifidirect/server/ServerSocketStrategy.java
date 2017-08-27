package com.sunny.youyun.wifidirect.server;

import java.net.Socket;

/**
 * This interface define the action serverSocket can do when they receive a socket
 */
public interface ServerSocketStrategy {
    void service(Socket socket);
}
