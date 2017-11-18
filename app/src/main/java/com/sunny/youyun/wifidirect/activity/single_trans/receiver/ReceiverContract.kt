package com.sunny.youyun.wifidirect.activity.single_trans.receiver

import android.net.wifi.p2p.WifiP2pManager

import com.sunny.youyun.mvp.BaseModel
import com.sunny.youyun.mvp.BasePresenter
import com.sunny.youyun.mvp.BaseView

import java.io.IOException

/**
 * Created by Sunny on 2017/8/10 0010.
 */

interface ReceiverContract {
    interface View : BaseView

    interface Model : BaseModel

    abstract class Presenter : BasePresenter<View, Model>()
}
