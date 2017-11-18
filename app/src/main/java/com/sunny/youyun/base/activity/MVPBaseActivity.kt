package com.sunny.youyun.base.activity

import android.os.Bundle
import android.support.annotation.CallSuper

import com.sunny.youyun.mvp.BasePresenter
import com.sunny.youyun.mvp.BaseView
import kotlin.properties.Delegates


/**
 * Created by Administrator on 2017/3/12 0012.
 */

abstract class MVPBaseActivity<P : BasePresenter<*, *>> : YouyunActivity(), BaseView {

    protected var mPresenter: P by Delegates.notNull()

    @CallSuper      //表示
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("onCreate presenter")
        mPresenter = onCreatePresenter()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        //当Activity被销毁时，取消所有订阅
        mPresenter.clearAllDisposable()
    }
    protected abstract fun onCreatePresenter(): P
    override fun allDataLoadFinish() {

    }
}
