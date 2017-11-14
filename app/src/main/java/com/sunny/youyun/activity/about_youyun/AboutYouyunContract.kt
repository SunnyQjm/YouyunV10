package com.sunny.youyun.activity.about_youyun

import com.sunny.youyun.mvp.BaseModel
import com.sunny.youyun.mvp.BasePresenter
import com.sunny.youyun.mvp.BaseView

/**
 * Created by Sunny on 2017/8/31 0031.
 */

interface AboutYouyunContract {
    interface View : BaseView

    interface Model : BaseModel

    abstract class Presenter : BasePresenter<View, Model>()
}
