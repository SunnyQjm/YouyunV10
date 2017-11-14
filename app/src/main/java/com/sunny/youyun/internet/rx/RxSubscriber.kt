package com.sunny.youyun.internet.rx

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by sunny on 17-11-14.
 */
abstract class RxObserver<T> : Observer<T>{
    override fun onComplete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNext(t: T) {
        _onNext(t)
    }

    override fun onSubscribe(d: Disposable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    abstract fun _onNext(t: T)

    abstract fun _onError(msg: String)
}