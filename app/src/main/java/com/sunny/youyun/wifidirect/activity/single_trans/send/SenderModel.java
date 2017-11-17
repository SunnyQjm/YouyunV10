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
}
