package com.sunny.youyun.wifidirect.client;

public class FileMsgTestMain {
    public static void main(String[] args) {
    }
    }
//        Scanner scanner = new Scanner(System.in);
//        String input = scanner.nextLine();
//        ClientSocketManager.ThreadTaskBuilder.ThreadTaskCallback threadTaskCallback =
//                new ClientSocketManager.ThreadTaskBuilder.ThreadTaskCallback() {
//                    @Override
//                    public void onError(String errInfo) {
//
//                    }
//
//                    @Override
//                    public void onSuccess() {
//
//                    }
//                };
//        SendFileCallBack sendFileCallBack = new SendFileCallBack() {
//            @Override
//            public void onBegin(int position) {
//
//            }
//
//            @Override
//            public void onProcess(TransLocalFile transLocalFile, int process, int position) {
//
//            }
//
//            @Override
//            public void onEnd(TransLocalFile localFile, int position) {
//
//            }
//
//            @Override
//            public void onError(TransLocalFile localFile, int position) {
//
//            }
//        };
//        while (!input.equals("quit")){
//            args = input.split(" ");
//            if(args.length != 2){
//                System.out.println("输入参数有误，请重新输入");
//                input = scanner.nextLine();
//                continue;
//            }
//            System.out.println(Arrays.toString(args));
//            switch (args[0]){
//                case "upload":
//                    MyThreadPool.getInstance()
//                            .submit(ClientSocketManager.ThreadTaskBuilder.newSendFileTask(
//                                    args[1], "127.0.0.1", SocketConfig.FileListenPort,
//                                    threadTaskCallback, sendFileCallBack
//                            ));
//                    break;
//                case "msg":
//                    String[] finalArgs = args;
//                    MyThreadPool.getInstance()
//                            .submit(ClientSocketManager.ThreadTaskBuilder.newSendTextTask(
//                                    "127.0.0.1", SocketConfig.FileListenPort, args[1],
//                                    new ClientSocketManager.ThreadTaskBuilder.ThreadTaskCallback() {
//                                        @Override
//                                        public void onError(String errInfo) {
//                                            System.out.println("发送文字失败: " + errInfo);
//                                        }
//
//                                        @Override
//                                        public void onSuccess() {
//                                            System.out.println("发送文字成功：" + finalArgs[1]);
//                                        }
//                                    }
//                            ));
//                    break;
//            }
//            input = scanner.nextLine();
//        }
//    }
//}
