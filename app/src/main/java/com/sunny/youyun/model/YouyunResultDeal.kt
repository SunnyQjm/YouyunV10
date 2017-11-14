package com.sunny.youyun.model

import com.sunny.youyun.base.entity.MultiItemEntity
import com.sunny.youyun.internet.api.ApiInfo
import com.sunny.youyun.model.response_body.BaseResponseBody

import java.util.Collections

/**
 * Created by Sunny on 2017/10/12 0012.
 */

object YouyunResultDeal {

    fun <T> dealData(responseBody: BaseResponseBody<Array<T>>, mList: MutableList<T>,
                     isRefresh: Boolean): Int {
        if (responseBody.success && responseBody.data != null) {
            if (isRefresh)
                mList.clear()
            Collections.addAll(mList, *responseBody.data!!)
            return if (responseBody.data!!.size < ApiInfo.GET_DEFAULT_SIZE) {
                ApiInfo.RESULT_DEAL_TYPE_LOAD_FINISH
            } else ApiInfo.RESULT_DEAL_TYPE_SUCCESS
        } else {
            return ApiInfo.RESULT_DEAL_TYPE_FAIL
        }
    }

    fun <T> dealData(result: Array<T>, mList: MutableList<T>, isRefresh: Boolean): Int {
        if (isRefresh)
            mList.clear()
        val i = mList.addAll(result)

        return if (result.size < ApiInfo.GET_DEFAULT_SIZE) {
            ApiInfo.RESULT_DEAL_TYPE_LOAD_FINISH
        } else ApiInfo.RESULT_DEAL_TYPE_SUCCESS
    }

    fun <T : MultiItemEntity> dealMultiData(result: Array<T>, mList: MutableList<MultiItemEntity>, isRefresh: Boolean,
                                            addAll: (Array<T>, MutableList<MultiItemEntity>) -> Unit): Int {
        if (isRefresh)
            mList.clear()
        addAll(result, mList)
        return if (result.size < ApiInfo.GET_DEFAULT_SIZE) {
            ApiInfo.RESULT_DEAL_TYPE_LOAD_FINISH
        } else ApiInfo.RESULT_DEAL_TYPE_SUCCESS
    }

    fun <T : MultiItemEntity> dealMultiData(result: Array<T>, mList: MutableList<MultiItemEntity>, isRefresh: Boolean): Int {
        return dealMultiData(result, mList, isRefresh) { r, l ->
            l.addAll(r)
        }
    }

    /**
     * 处理数据处理结果
     *
     * @param code     处理结果标识
     * @param listener 回调
     */
    fun deal(code: Int, listener: OnResultListener?) {
        when (code) {
            ApiInfo.RESULT_DEAL_TYPE_FAIL -> listener?.onFail()
            ApiInfo.RESULT_DEAL_TYPE_LOAD_FINISH -> listener?.onLoadFinish()
            ApiInfo.RESULT_DEAL_TYPE_SUCCESS -> listener?.onSuccess()
        }
    }


    interface OnResultListener {
        fun onSuccess()

        fun onLoadFinish()

        fun onFail()
    }
}
