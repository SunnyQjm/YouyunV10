package com.sunny.youyun.internet.rx

import com.sunny.youyun.YouyunResultDeal
import com.sunny.youyun.internet.api.ApiInfo
import com.sunny.youyun.internet.exception.LoginTokenInvalidException
import com.sunny.youyun.internet.exception.NotLoginException
import com.sunny.youyun.internet.exception.ParamNotCorrectException
import com.sunny.youyun.model.response_body.BaseResponseBody
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Created by sunny on 17-11-14.
 */
object RxresultHelper {
    fun <T> handleResult(): ObservableTransformer<BaseResponseBody<T>, T> {
        return ObservableTransformer { upstream ->
            return@ObservableTransformer upstream
                    .flatMap { result ->
                        if (result.success and (result.data != null)) {
                            return@flatMap Observable.just(result.data)
                        }
                        when (result.code) {
                            ApiInfo.STATUS_CODE_LOGIN_INVALID -> {
                                return@flatMap Observable.error<T>(LoginTokenInvalidException("登录失效"))
                            }
                            ApiInfo.STATUS_CODE_NOT_LOGIN -> {
                                return@flatMap Observable.error<T>(NotLoginException("未登录"))
                            }
                            ApiInfo.STATUS_CODE_PARAM_NOT_COMPLETE -> {
                                return@flatMap Observable.error<T>(ParamNotCorrectException("参数无效"))
                            }
                        }
                        return@flatMap Observable.empty<T>()
                    }
        }
    }


    fun <T> handlePageResult(mList: MutableList<T>, isRefresh: Boolean)
            : ObservableTransformer<BaseResponseBody<Array<T>>, Int> {
        return ObservableTransformer { upstream ->
            return@ObservableTransformer upstream
                    .compose(handleResult())
                    .flatMap { result ->
                        return@flatMap Observable
                                .just(YouyunResultDeal.dealData(result = result, mList = mList, isRefresh = isRefresh))

                    }
        }
    }

}