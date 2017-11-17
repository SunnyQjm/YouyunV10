package com.sunny.youyun.wifidirect.activity.single_trans.send;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/10 0010.
 */

public class SenderPresenter extends SenderContract.Presenter{
    SenderPresenter(SenderActivity senderFragment) {
        mView = senderFragment;
        mModel = new SenderModel(this);
    }

    @Override
    protected void start() {
    }
}
