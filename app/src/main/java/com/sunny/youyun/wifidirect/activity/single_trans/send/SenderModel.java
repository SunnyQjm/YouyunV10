package com.sunny.youyun.wifidirect.activity.single_trans.send;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.utils.UUIDUtil;
import com.sunny.youyun.wifidirect.config.EventConfig;
import com.sunny.youyun.wifidirect.config.SocketConfig;
import com.sunny.youyun.wifidirect.event.BaseEvent;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;
import com.sunny.youyun.wifidirect.utils.EventRxBus;
import com.sunny.youyun.wifidirect.utils.MyThreadPool;
import com.sunny.youyun.wifidirect.utils.ProtocolStringBuilder;
import com.sunny.youyun.wifidirect.utils.SocketUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Sunny on 2017/8/10 0010.
 */

class SenderModel implements SenderContract.Model {
    private final SenderPresenter mPresenter;
    private final String key = UUIDUtil.getUUID();

    SenderModel(SenderPresenter senderPresenter) {
        mPresenter = senderPresenter;
    }

    @Override
    public void begin() throws IOException {
//        EventRxBus.getInstance().subscribe(key, event -> {
//            switch (event.getCode()){
//                case EventConfig.FORWARD_SIGNAL_ADD:
//                    break;
//                case EventConfig.FORWARD_SIGNAL_DELETE:
//                    break;
//                case EventConfig.IP_CHANGE:
//                    if(event.getData() instanceof String){
//                        SingleTransManager.getInstance().getTargetInfo().setIp((String) event.getData());
//                        System.out.println(SingleTransManager.getInstance().getTargetInfo());
//                        Logger.i("targetInfo: " + SingleTransManager.getInstance().getTargetInfo());
//                        Logger.i("myInfo: " + SingleTransManager.getInstance().getMyInfo());
//                        Logger.i("groupInfo: " + DeviceInfoManager.getInstance().getGroupOwnerIp());
//                        mPresenter.connectSuccess();
//                        exit();
//                    }
//                    break;
//            }
//        }, throwable -> Logger.e(throwable, "EventRxBusErr"));
        startSingleTaskServer(SocketConfig.singleTaskPort);
    }

    /**
     * 开始等待一个用户接入
     *
     * @param port
     */
    private void startSingleTaskServer(int port) {
        MyThreadPool.getInstance()
                .submit(() -> {
                    SocketUtils su = null;
                    try {
                        ServerSocket serverSocket = new ServerSocket(port);
                        Socket socket = serverSocket.accept();
                        su = new SocketUtils(socket);
                        String str = su.readUTF();
                        String[] results = ProtocolStringBuilder.getInfoArr(str);
                        int code = ProtocolStringBuilder.getCode(str);
                        su.writeUTF(socket.getInetAddress().getHostAddress());
                        EventBus.getDefault()
                                .post(new BaseEvent<>(
                                        EventConfig.IP_CHANGE, "IP", socket.getInetAddress().getHostAddress()
                                ));
//                        EventRxBus.getInstance().post(new BaseEvent<>(
//                                EventConfig.IP_CHANGE, "IP", socket.getInetAddress().getHostAddress()
//                        ));
                    } catch (IOException e) {
                        Logger.e(e, "startSingleTaskServer");
                    } finally {
                        if (su != null)
                            try {
                                su.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                });
    }

    @Override
    public void connect(final WifiP2pDevice device, @Nullable WifiP2pManager.ActionListener listener) {
        //如果之前正在连接或已连接，则先断开连接
        WifiDirectManager.getInstance().cancelConnect(null);
        WifiDirectManager.getInstance().disConnect();

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        WifiDirectManager.getInstance().connect(config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("连接请求发送成功");
            }

            @Override
            public void onFailure(int reason) {
                mPresenter.showError("连接失败");
                Logger.e("连接失败：" + reason);
            }
        });
    }

    @Override
    public void exit() {
        EventRxBus.getInstance().unSubscribe(key);
    }
}
