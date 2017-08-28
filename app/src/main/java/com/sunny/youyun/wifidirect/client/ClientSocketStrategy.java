package com.sunny.youyun.wifidirect.client;

import com.sunny.youyun.wifidirect.model.TransLocalFile;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public interface ClientSocketStrategy {

    /**
     * 通过Socket发送简单字符信息
     *
     * @param socket 操作的套接字对象
     * @param info   要发送的信息
     * @throws IOException
     */
    void sendText(Socket socket, String info) throws IOException;

    /**
     * This method indicate to send a Single File to other device by socket
     * @param socket            the socket connect between your device and the destination
     * @param file              the file you want to send
     */
    boolean sendSingleFile(Socket socket, File file, List<TransLocalFile> mList);

}
