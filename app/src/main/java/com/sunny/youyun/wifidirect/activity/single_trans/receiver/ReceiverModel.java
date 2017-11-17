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
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;
import com.sunny.youyun.wifidirect.utils.EventRxBus;
import com.sunny.youyun.wifidirect.utils.MyThreadPool;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/10 0010.
 */

class ReceiverModel implements ReceiverContract.Model {
    private ReceiverPresenter mPresenter;
    private final String uuid = UUIDUtil.getUUID();
    private final Handler handler = new Handler();

    ReceiverModel(ReceiverPresenter receiverPresenter) {
        mPresenter = receiverPresenter;
    }
}
