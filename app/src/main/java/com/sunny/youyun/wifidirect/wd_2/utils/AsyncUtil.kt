package com.sunny.youyun.wifidirect.wd_2.utils

import com.sunny.youyun.wifidirect.wd_2.rx.RxSchedulersHelper
import io.reactivex.Observable

/**
 * Created by sunny on 17-11-16.
 */

fun doInMainThread(block: ()->Unit){
    Observable.just(1)
            .compose(RxSchedulersHelper.io_main())
            .subscribe {
                block.invoke()
            }
}

fun doInIOThread(block: ()->Unit){
    Observable.just(1)
            .compose(RxSchedulersHelper.io_io())
            .subscribe{
                block.invoke()
            }
}