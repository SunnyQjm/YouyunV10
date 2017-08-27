package com.sunny.youyun.wifidirect.activity.single_trans.send;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.Nullable;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/10 0010.
 */

interface SenderContract {
    interface View extends BaseView {
        void connectSuccess();
    }

    interface Model extends BaseModel {
        void begin() throws IOException;
        void connect(final WifiP2pDevice device, @Nullable WifiP2pManager.ActionListener listener);
        void exit();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void connect(final WifiP2pDevice device, @Nullable WifiP2pManager.ActionListener listener);
        abstract void connectSuccess();
        abstract void exit();
    }
}
