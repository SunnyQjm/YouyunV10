package com.sunny.youyun.activity.about_youyun

import java.io.IOException

/**
 * Created by Sunny on 2017/8/31 0031.
 */

class AboutYouyunPresenter(aboutYouyunActivity: AboutYouyunActivity) : AboutYouyunContract.Presenter() {
    init {
        mView = aboutYouyunActivity
        mModel = AboutYouyunModel(this)
    }

    @Throws(IOException::class)
    override fun start() {

    }
}
