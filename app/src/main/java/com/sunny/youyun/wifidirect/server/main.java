package com.sunny.youyun.wifidirect.server;

import java.io.IOException;

public class main {
    public static void main(String[] args) throws InterruptedException, IOException {
//        ReadFileCallback readFileCallback = new ReadFileCallback() {
//            @Override
//            public void onBegin(int position) {
//                System.out.println("开始接收文件");
//            }
//
//            @Override
//            public void onProcess(TransLocalFile transLocalFile, int process, int position) {
//                System.out.println("接收进度：" + process);
//            }
//
//            @Override
//            public void onEnd(TransLocalFile localFile, int position) {
//                System.out.println("文件接收结束：" + localFile);
//            }
//
//            @Override
//            public void onError(TransLocalFile localFile, int position) {
//                System.out.println("文件接收错误");
//            }
//        };
//
//
//        ServerSocketManager serverSocketManager = new ServerSocketManager.Builder()
//                .strategy(new SingleTransMode(App.mList_ReceiveRecord))
//                .port(SocketConfig.FileListenPort)
//                .build();
//        MyThreadPool.getInstance()
//                .submit(serverSocketManager);
//
//        ForwardServer forwardServer = new ForwardServer(SocketConfig.FORWARD_ITEM_LISTEN_PORT);
//        MyThreadPool.getInstance()
//                .submit(forwardServer);

//        MyThreadPool.getInstance().shutDown();
//        TimeUnit.SECONDS.sleep(3);
//        System.out.println(serverSocketManager.isAlive());
//        serverSocketManager.stopListen();
//        System.out.println(serverSocketManager.isAlive());
    }

}
