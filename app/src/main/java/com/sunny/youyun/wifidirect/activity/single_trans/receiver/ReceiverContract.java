package com.sunny.youyun.wifidirect.activity.single_trans.receiver;

import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.Nullable;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/10 0010.
 */

interface ReceiverContract {
    interface View extends BaseView {
        void connectSuccess();
    }

    interface Model extends BaseModel {
        void begin() throws IOException;
        void connect(final String macAddr, @Nullable WifiP2pManager.ActionListener listener);
        void exit();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void connect(final String macAddr, @Nullable WifiP2pManager.ActionListener listener);
        abstract void connectSuccess();
        abstract void exit();
    }
}
