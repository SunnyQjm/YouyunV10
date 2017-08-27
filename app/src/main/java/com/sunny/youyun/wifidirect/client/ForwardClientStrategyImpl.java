package com.sunny.youyun.wifidirect.client;

import com.sunny.youyun.wifidirect.info.CODE_TABLE;
import com.sunny.youyun.wifidirect.model.DeviceInfo;
import com.sunny.youyun.wifidirect.model.SocketRequestBody;
import com.sunny.youyun.wifidirect.utils.CheckSumUtil;
import com.sunny.youyun.wifidirect.utils.GsonUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class ForwardClientStrategyImpl implements ForwardClientStrategy{

    @Override
    public void broadcastDeviceInfo(int port, DeviceInfo deviceInfo) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();

        SocketRequestBody<DeviceInfo> body = new SocketRequestBody<>(CODE_TABLE.REQUEST_ADD_FORWARD_ITEM, "add item", deviceInfo);
        String info = GsonUtil.getInstance().toJson(body);

        //准备数据，并计算出32bit的CRC校验码放在数据末尾
        byte[] data = info.getBytes("utf-8");
        byte[] dataWithCRC = Arrays.copyOf(data, data.length + 4);
        byte[] checkSum = CheckSumUtil.getCRC32Value(data);
        System.arraycopy(checkSum, 4, dataWithCRC, data.length, dataWithCRC.length - data.length);

        //开始发送数据包，不管服务器在不在线，只管发
        DatagramPacket datagramPacket = new DatagramPacket(
                dataWithCRC, dataWithCRC.length, new InetSocketAddress(
                        "255.255.255.255", port));
        datagramSocket.send(datagramPacket);
        System.out.println("send : " + info);
        datagramSocket.close();
    }

    @Override
    public void deleteDeviceInfo(int port, DeviceInfo deviceInfo) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();

        SocketRequestBody<DeviceInfo> body = new SocketRequestBody<>(CODE_TABLE.REQUEST_DELETE_FORWARD_ITEM, "delete item", deviceInfo);
        String info = GsonUtil.getInstance().toJson(body);

        //准备数据，并计算出32bit的CRC校验码放在数据末尾
        byte[] data = info.getBytes("utf-8");
        byte[] dataWithCRC = Arrays.copyOf(data, data.length + 4);
        byte[] checkSum = CheckSumUtil.getCRC32Value(data);
        System.arraycopy(checkSum, 4, dataWithCRC, data.length, dataWithCRC.length - data.length);

        //开始发送数据包，不管服务器在不在线，只管发
        DatagramPacket datagramPacket = new DatagramPacket(
                dataWithCRC, dataWithCRC.length, new InetSocketAddress(
                "255.255.255.255", port));
        datagramSocket.send(datagramPacket);
        System.out.println("delete : " + info);
        datagramSocket.close();
    }
}
