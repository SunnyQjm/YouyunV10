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
    protected void start() throws IOException {

    }
}
