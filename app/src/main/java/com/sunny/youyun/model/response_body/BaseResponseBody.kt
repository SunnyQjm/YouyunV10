package com.sunny.youyun.model.response_body

import java.io.Serializable

/**
 * Created by Administrator on 2017/3/30 0030.
 */

open class BaseResponseBody<T>(var success: Boolean = false, var status: Int = 0,
                               var code: Int = 0, var msg: String? = null,
                               var data: T? = null) : Serializable {

    fun isSuccess(): Boolean = success

    /**
     * success : true
     * status : 200
     * msg :
     */
    override fun toString(): String {
        return "BaseResponseBody{" +
                "success=" + success +
                ", status=" + status +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}'
    }
}
