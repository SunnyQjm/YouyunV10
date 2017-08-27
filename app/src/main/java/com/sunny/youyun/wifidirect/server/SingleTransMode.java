package com.sunny.youyun.wifidirect.server;


import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.wifidirect.config.EventConfig;
import com.sunny.youyun.wifidirect.event.BaseEvent;
import com.sunny.youyun.wifidirect.info.CODE_TABLE;
import com.sunny.youyun.wifidirect.model.SocketRequestBody;
import com.sunny.youyun.wifidirect.model.TransLocalFile;
import com.sunny.youyun.wifidirect.utils.EventRxBus;
import com.sunny.youyun.wifidirect.utils.GsonUtil;
import com.sunny.youyun.wifidirect.utils.ProtocolStringBuilder;
import com.sunny.youyun.wifidirect.utils.SocketUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class SingleTransMode implements ServerSocketStrategy {

    private final List<TransLocalFile> mListReceive;
    public SingleTransMode(List<TransLocalFile> list) {
        mListReceive = list;
    }


    @Override
    public void service(Socket socket) {
        SocketUtils socketUtils = null;
        try {
            socketUtils = new SocketUtils(socket);
            String str = socketUtils.readUTF();
            String[] results = ProtocolStringBuilder.getInfoArr(str);
            int code = ProtocolStringBuilder.getCode(str);
            switch (code) {
                case CODE_TABLE.REQUEST_SIMPLE_TEXT:    //传递简单信息
                    System.out.println(results[1]);
                    //将对方的IP写回，以便对方获取IP
                    socketUtils.writeUTF(socket.getInetAddress().getHostAddress());
                    break;
                case CODE_TABLE.CHANGE_IP:
                    socketUtils.writeUTF(socket.getInetAddress().getHostAddress());
                    EventRxBus.getInstance().post(new BaseEvent<>(
                            EventConfig.IP_CHANGE, "IP", socket.getInetAddress().getHostAddress()
                    ));
                case CODE_TABLE.REQUEST_SINGLE_FILE:
                    SocketRequestBody<TransLocalFile> body = GsonUtil.getInstance().fromJson(results[1],
                            new TypeToken<SocketRequestBody<TransLocalFile>>() {
                            }.getType());
                    TransLocalFile localFile = body.getObj();
                    String path = FileUtils.getWifiDirectPath() + localFile.getName();
                    System.out.println("传输的文件名：" + localFile.getName());
                    System.out.println("文件的大小为：" + localFile.getSize());
                    System.out.println("文件接收路径：" + path);
                    System.out.println();
                    int position;
                    synchronized (SocketUtils.class){
                        position = mListReceive.size();
                        mListReceive.add(localFile);
                    }
                    socketUtils.readFile(path, localFile, position);
                    System.out.println("文件读取完毕");
                    break;
                default:

            }
        } catch (IOException e) {
            Logger.e(e, "客户端关闭了连接");
        } finally {
            if (socketUtils != null) {
                try {
                    socketUtils.close();
                } catch (IOException e) {
                    Logger.e(e, "socket 关闭失败");
                }
            }
        }
    }
}
