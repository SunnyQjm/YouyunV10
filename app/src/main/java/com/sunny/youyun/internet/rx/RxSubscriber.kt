package com.sunny.youyun.internet.rx

import com.sunny.youyun.model.event.RefreshEvent
import com.sunny.youyun.mvp.BasePresenter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

/**
 * Created by sunny on 17-11-14.
 */
abstract class RxObserver<T>(val mPresenter: BasePresenter<*, *>) : Observer<T> {

    override fun onComplete() {
    }

    override fun onError(e: Throwable) {
        com.orhanobut.logger.Logger.e("Error", e)
    }

    override fun onNext(t: T) {
        _onNext(t)
        EventBus.getDefault()
                .post(RefreshEvent())
    }

    override fun onSubscribe(d: Disposable) {
        mPresenter.addSubscription(d)
    }

    abstract fun _onNext(t: T)

}