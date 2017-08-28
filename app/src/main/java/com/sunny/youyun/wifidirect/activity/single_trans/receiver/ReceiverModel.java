package com.sunny.youyun.wifidirect.activity.single_trans.receiver;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.utils.UUIDUtil;
import com.sunny.youyun.wifidirect.client.ClientSocketManager;
import com.sunny.youyun.wifidirect.config.EventConfig;
import com.sunny.youyun.wifidirect.config.SocketConfig;
import com.sunny.youyun.wifidirect.event.BaseEvent;
import com.sunny.youyun.wifidirect.manager.DeviceInfoManager;
import com.sunny.youyun.wifidirect.manager.SingleTransManager;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;
import com.sunny.youyun.wifidirect.utils.EventRxBus;
import com.sunny.youyun.wifidirect.utils.MyThreadPool;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/10 0010.
 */

class ReceiverModel implements ReceiverContract.Model{
    private ReceiverPresenter mPresenter;
    private final String uuid = UUIDUtil.getUUID();
    private final Handler handler  = new Handler();
    ReceiverModel(ReceiverPresenter receiverPresenter) {
        mPresenter = receiverPresenter;
    }

    @Override
    public void begin() throws IOException {
        WifiDirectManager.WifiDirectListeners.getInstance().bindConnectionInfoListener(info -> {
            if(info.groupFormed)
                changeIP();
        });
    }

    private void changeIP(){
        EventRxBus.getInstance().subscribe(uuid, event -> {
            if(event.getCode() == EventConfig.IP_CHANGE){
                if(event.getData() instanceof String){
                    SingleTransManager.getInstance().getMyInfo().setIp((String) event.getData());
                    SingleTransManager.getInstance().getTargetInfo().setIp(DeviceInfoManager.getInstance().getGroupOwnerIp());
                    mPresenter.connectSuccess();
                    EventRxBus.getInstance().unSubscribe(uuid);
                }
            }
        }, throwable -> Logger.e(throwable, "Ip回调错误"));
        if (!DeviceInfoManager.getInstance().isGroupOwner()) {
            MyThreadPool.getInstance()
                    .submit(() -> {
                        String ip = "";
                        try {
                            ip = ClientSocketManager.getInstance().askIP(DeviceInfoManager.getInstance().getGroupOwnerIp(), SocketConfig.singleTaskPort);
                        } catch (IOException e) {
                            Logger.e(e, "获取IP失败");
                            handler.post(()->{
                                mPresenter.showError("连接失败");
                            });
                        }
                        System.out.println("我的IP：" + ip);
                        Logger.i("我的IP：" + ip);
                        if (ip.equals("")) {
                            return;
                        }
                        EventRxBus.getInstance().post(new BaseEvent<>(
                                EventConfig.IP_CHANGE, "传递ip", ip
                        ));
                    });
        }
    }

    @Override
    public void connect(final String macAddr, @Nullable WifiP2pManager.ActionListener listener) {
        //如果之前正在连接，则先断开连接
        WifiDirectManager.getInstance().cancelConnect(null);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = macAddr;
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
        EventRxBus.getInstance().unSubscribe(uuid);
    }

}
