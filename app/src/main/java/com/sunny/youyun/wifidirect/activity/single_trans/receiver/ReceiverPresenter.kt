package com.sunny.youyun.wifidirect.activity.single_trans.receiver

import java.io.IOException

/**
 * Created by Sunny on 2017/8/10 0010.
 */

class ReceiverPresenter internal constructor(receiverFragment: ReceiverActivity) : ReceiverContract.Presenter() {
    init {
        mView = receiverFragment
        mModel = ReceiverModel(this)
    }

    @Throws(IOException::class)
    override fun start() {

    }
}
