package com.sunny.youyun.wifidirect.activity.single_trans.send;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/10 0010.
 */

class SenderPresenter extends SenderContract.Presenter{
    SenderPresenter(SenderActivity senderFragment) {
        mView = senderFragment;
        mModel = new SenderModel(this);
    }

    @Override
    protected void start() {
        try {
            mModel.begin();
        } catch (IOException e) {
            Logger.e(e, "初始化失败");
        }
    }

    @Override
    void connect(final WifiP2pDevice device, @Nullable WifiP2pManager.ActionListener listener) {
        mModel.connect(device, listener);
    }

    @Override
    void connectSuccess() {
        mView.connectSuccess();
    }

    @Override
    void exit() {
        mModel.exit();
    }
}
