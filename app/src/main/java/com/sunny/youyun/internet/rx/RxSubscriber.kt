package com.sunny.youyun.internet.rx

import com.sunny.youyun.model.event.RefreshEvent
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

/**
 * Created by sunny on 17-11-14.
 */
abstract class RxObserver<T> : Observer<T>{
    override fun onComplete() {
    }

    override fun onError(e: Throwable) {
        _onError(e.toString())
    }

    override fun onNext(t: T) {
        _onNext(t)
        EventBus.getDefault()
                .post(RefreshEvent())
    }

    override fun onSubscribe(d: Disposable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    abstract fun _onNext(t: T)

    abstract fun _onError(msg: String)
}