package com.sunny.youyun.wifidirect.activity.single_trans.send

/**
 * Created by Sunny on 2017/8/10 0010.
 */

class SenderPresenter internal constructor(senderFragment: SenderActivity) : SenderContract.Presenter() {
    init {
        mView = senderFragment
        mModel = SenderModel(this)
    }

    override fun start() {}
}
