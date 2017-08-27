package com.sunny.youyun.wifidirect.client;


import android.support.annotation.NonNull;

import com.sunny.youyun.wifidirect.info.CODE_TABLE;
import com.sunny.youyun.wifidirect.model.SocketRequestBody;
import com.sunny.youyun.wifidirect.model.TransLocalFile;
import com.sunny.youyun.wifidirect.utils.GsonUtil;
import com.sunny.youyun.wifidirect.utils.ProtocolStringBuilder;
import com.sunny.youyun.wifidirect.utils.SocketUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * Created by Sunny on 2017/4/21 0021.
 */
public enum  ClientSocketManager implements ClientSocketStrategy {

    INSTANCE;
    public static ClientSocketManager getInstance() {
        return INSTANCE;
    }


    public String askIP(String ip, int port) throws IOException {
        Socket socket = new Socket(ip, port);
        SocketUtils su = new SocketUtils(socket);
        String str = "";
        str = ProtocolStringBuilder.getInstance()
                .contract(CODE_TABLE.CHANGE_IP)
                .contract(str)
                .toString();
        su.writeUTF(str);
        String IP = su.readUTF();
        su.close();
        return IP;
    }
    private <T> void send(final Socket socket, final SocketRequestBody<T> body, final int code) throws IOException {
        SocketUtils su = new SocketUtils(socket);

        String str = GsonUtil.getInstance().toJson(body);
        str = ProtocolStringBuilder.getInstance()
                .contract(code)
                .contract(str)
                .toString();

        su.writeUTF(str);      //将信息发给服务端
        String ret = su.readUTF();
        System.out.println("服务器返回来的消息为: " + ret);
        su.close();
    }

    @Override
    public void sendText(Socket socket, String info) throws IOException {
        SocketRequestBody<String> body = new SocketRequestBody<>(CODE_TABLE.REQUEST_SIMPLE_TEXT, "send text",
                info);
        send(socket, body, CODE_TABLE.REQUEST_SIMPLE_TEXT);
    }

    @Override
    public void sendSingleFile(final Socket socket, final File file, @NonNull final List<TransLocalFile> mList) {
        SocketUtils socketUtils = null;
        try {
            socketUtils = new SocketUtils(socket);
            socketUtils.writeFile(file, mList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketUtils != null)
                try {
                    socketUtils.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


    /**
     * Created by Sunny on 2017/4/27 0027.
     */
    public static class ThreadTaskBuilder {

        public interface ThreadTaskCallback {
            void onError(String errInfo);

            void onSuccess();
        }

        /**
         * 向指定IP和端口的服务器发送一串文字信息
         *
         * @param IP       目的IP
         * @param port     目的端口
         * @param info     要发送的字符串
         * @param callback 发送消息回调
         * @return 构造生成的Runnable对象
         */
        public static Runnable newSendTextTask(final String IP, final int port, final String info, final ThreadTaskCallback callback) {
            return () -> {
                Socket socket = null;
                try {
                    socket = new Socket(IP, port);
                    getInstance().sendText(socket, info);
                    callback.onSuccess();
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onError("连接服务器失败");
                }
            };
        }


        public static FutureTask<File> newSendFileTask(final String path,
                                                       final String IP, final int port,
                                                       final ThreadTaskCallback callback, @NonNull final List<TransLocalFile> mList) {
            return new FutureTask<>(() -> {
                Socket socket = null;
                socket = new Socket(IP, port);
                File file = new File(path);
                if (!file.exists()) {
                    return null;
                }
                getInstance().sendSingleFile(socket, file, mList);
                callback.onSuccess();
                System.out.println("over");
                if (socket.isClosed()) {
                    socket.close();
                }
                return file;
            });
        }


        /**
         * 通过InetAddress获取MAC地址
         *
         * @param ia
         * @return
         * @throws SocketException
         */
        private static String getLocalMac(InetAddress ia) throws SocketException {
            //获取网卡，获取地址
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            if (mac == null) {
                return "";
            }
            //        System.out.println("mac数组长度：" + mac.length);
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append(":");
                }
                //字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                //            System.out.println("每8位:" + str);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString();
        }


    }
}
