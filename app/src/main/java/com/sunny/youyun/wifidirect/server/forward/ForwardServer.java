package com.sunny.youyun.wifidirect.server.forward;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.wifidirect.base.BaseRunnable;
import com.sunny.youyun.wifidirect.utils.MyThreadPool;
import com.sunny.youyun.wifidirect.utils.Tool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ForwardServer extends BaseRunnable {
    private static final int BUFFER_SIZE = 1024;
    private final int port;
    private ForwardServerDataDealRunnable fsddr;
    private DatagramSocket ds = null;

    public ForwardServer(int port) {
        System.out.println("ForwardServer is ready！！");
        this.port = port;
    }


    @Override
    public void run() {
        isStop = false;
        try {
            ds = new DatagramSocket(port);
            DatagramPacket dp = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE,
                    InetAddress.getLocalHost(), port);
            fsddr = new ForwardServerDataDealRunnable();

            //在另一个线程异步处理节点服务器发上来的心跳包
            MyThreadPool.getInstance()
                    .submit(fsddr);

            Logger.i("开始接收心跳包");
            while(!MyThreadPool.getInstance().isShutdownJudge() && !isStop()){
                ds.receive(dp);
                //收到一个心跳包就将它放到等待队列
                fsddr.add(Tool.subByteArray(dp.getData(), 0, dp.getLength()));
            }
        } catch (IOException e) {
            Logger.e(e, "停止接收心跳包");
        }
    }


    @Override
    public void stop(){
        isStop = true;
        if(ds != null && !ds.isClosed())
            ds.close();
        if(fsddr != null)
            fsddr.stop();
    }
}
