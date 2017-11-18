package com.sunny.youyun.wifidirect.activity.single_trans.trans

import com.sunny.youyun.mvp.BaseModel
import com.sunny.youyun.mvp.BasePresenter
import com.sunny.youyun.mvp.BaseView
import com.sunny.youyun.wifidirect.model.TransLocalFile

/**
 * Created by Sunny on 2017/8/11 0011.
 */

interface TransContract {
    interface View : BaseView {
        fun updateItem(position: Int, transLocalFile: TransLocalFile)
        fun update()
    }

    interface Model : BaseModel {

        val data: List<TransLocalFile>
        fun begin()

        fun exit()

        fun send(ip: String, paths: Array<String>)

    }

    abstract class Presenter : BasePresenter<View, Model>() {

        abstract fun getData(): List<TransLocalFile>
        abstract fun exit()


        abstract fun send(ip: String, paths: Array<String>)
        abstract fun update()
        abstract fun updateItem(i: Int, transLocalFile: TransLocalFile)
    }
}
