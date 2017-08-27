package com.sunny.youyun.base;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;
import com.sunny.youyun.wifidirect.utils.NetworkUtils;


/**
 * Created by Sunny on 2017/5/12 0012.
 */

public abstract class WifiDirectBaseActivity<P extends BasePresenter> extends MVPBaseActivity<P> {
    protected WifiP2pManager.PeerListListener peerListListener;
    protected WifiP2pManager.ConnectionInfoListener connectionInfoListener;

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        //打开wifi
        WifiDirectManager.getInstance().setWifiDirectEnable(NetworkUtils.setWifiEnable(this, true));
        //开始接收转发表
//        WifiDirectManager.getInstance().beginReceiveDeviceInfo(SocketConfig.FORWARD_ITEM_LISTEN_PORT);
    }

    @Override
    @CallSuper
    protected void onPause() {
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWifiDirectListenerAndIntentFilter();
    }

    /**
     * 可复写定制自己的回调监听
     */
    protected void initWifiDirectListenerAndIntentFilter() {
        peerListListener = peers -> System.out.println("默认的PeerListListener回调");
        connectionInfoListener = info -> System.out.println("默认的ConnectInfoListen回调");
    }
}
