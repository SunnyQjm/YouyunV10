package com.sunny.youyun.wifidirect.activity.single_trans.trans

import com.sunny.youyun.wifidirect.model.TransLocalFile

/**
 * Created by Sunny on 2017/8/11 0011.
 */

class TransPresenter(transFragment: TransActivity) : TransContract.Presenter() {

    override fun getData(): List<TransLocalFile> = mModel.data

    init {
        mView = transFragment
        mModel = TransModel(this)
    }

    public override fun start() {
        mModel.begin()
    }

    override fun exit() {
        mModel.exit()
    }


    override fun send(ip: String, paths: Array<String>) {
        mModel.send(ip, paths)
    }

    override fun update() {
        mView.update()
    }

    override fun updateItem(i: Int, transLocalFile: TransLocalFile) {
        mView.updateItem(i, transLocalFile)
    }
}
