package com.sunny.youyun.wifidirect.server;


import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.wifidirect.event.FileTransEvent;
import com.sunny.youyun.wifidirect.info.CODE_TABLE;
import com.sunny.youyun.wifidirect.model.SocketRequestBody;
import com.sunny.youyun.wifidirect.model.TransLocalFile;
import com.sunny.youyun.wifidirect.utils.GsonUtil;
import com.sunny.youyun.wifidirect.utils.ProtocolStringBuilder;
import com.sunny.youyun.wifidirect.utils.SocketUtils;
import com.sunny.youyun.wifidirect.utils.TransRxBus;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public enum  SingleTransMode implements ServerSocketStrategy {

    INSTANCE;
    public static SingleTransMode getInstance(){
        return INSTANCE;
    }
    private volatile List<TransLocalFile> mListReceive;

    public void bindMList(@NonNull List<TransLocalFile> mListReceive){
        this.mListReceive = mListReceive;
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
                case CODE_TABLE.REQUEST_SINGLE_FILE:
                    SocketRequestBody<TransLocalFile> body = GsonUtil.getInstance().fromJson(results[1],
                            new TypeToken<SocketRequestBody<TransLocalFile>>() {
                            }.getType());
                    TransLocalFile transLocalFile = body.getObj();
                    transLocalFile.setFileTAG(TransLocalFile.TAG_RECEIVE);
                    String path = FileUtils.getWifiDirectPath() + transLocalFile.getName();
                    transLocalFile.setPath(path);
                    System.out.println("传输的文件名：" + transLocalFile.getName());
                    System.out.println("文件的大小为：" + transLocalFile.getSize());
                    System.out.println("文件接收路径：" + path);
                    System.out.println();
                    int position;
                    synchronized (SocketUtils.class) {
                        position = mListReceive.size();
                        mListReceive.add(transLocalFile);
                    }
                    receiveFile(socketUtils, position, transLocalFile);
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

    private void receiveFile(SocketUtils socketUtils, int position, TransLocalFile localFile) {
        try {
            socketUtils.readFile(localFile, new SocketUtils.FileTransCallback() {
                @Override
                public void onBegin() {
                    TransRxBus.getInstance().post(new FileTransEvent.Builder()
                            .already(0)
                            .total(0)
                            .done(false)
                            .position(position)
                            .type(FileTransEvent.Type.BEGIN)
                            .build());
                }

                @Override
                public void onProgress(FileTransEvent fileTransEvent) {
                    TransRxBus.getInstance().post(new FileTransEvent.Builder()
                            .already(fileTransEvent.getAlready())
                            .total(fileTransEvent.getTotal())
                            .done(fileTransEvent.isDone())
                            .position(position)
                            .type(FileTransEvent.Type.DOWNLOAD)
                            .build());
                }

                @Override
                public void onEnd() {
                    TransRxBus.getInstance().post(new FileTransEvent.Builder()
                            .already(0)
                            .total(0)
                            .done(false)
                            .position(position)
                            .type(FileTransEvent.Type.FINISH)
                            .build());
                }

                @Override
                public void onError(Exception e) {
                    TransRxBus.getInstance().post(new FileTransEvent.Builder()
                            .exception(e)
                            .position(position)
                            .type(FileTransEvent.Type.ERROR)
                            .build());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e("写入文件IO错误", e);
        }
    }
}
