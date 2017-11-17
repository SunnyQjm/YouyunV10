package com.sunny.youyun.wifidirect.activity.single_trans.receiver;

import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/10 0010.
 */

public class ReceiverPresenter extends ReceiverContract.Presenter{
    ReceiverPresenter(ReceiverActivity receiverFragment) {
        mView = receiverFragment;
        mModel = new ReceiverModel(this);
    }

    @Override
    protected void start() {
        try {
            mModel.begin();
        } catch (IOException e) {
            Logger.e("初始化失败");
        }
    }

    @Override
    void connect(final String macAddr, @Nullable WifiP2pManager.ActionListener listener) {
        mModel.connect(macAddr, listener);
    }


    @Override
    void exit() {
        mModel.exit();
    }
}
