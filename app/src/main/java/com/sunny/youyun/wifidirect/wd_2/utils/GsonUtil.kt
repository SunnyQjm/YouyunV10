package com.sunny.youyun.wifidirect.wd_2.utils

import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * Created by sunny on 17-11-17.
 */
object GsonUtil {
    @JvmStatic private val INSTANCE = Gson()

    @JvmStatic
    fun <T> json2Bean(json: String, beanClass: Class<T>): T = INSTANCE.fromJson<T>(json, beanClass)

    @JvmStatic
    fun <T> json2Bean(json: String, type: Type): T = INSTANCE.fromJson<T>(json, type)

    @JvmStatic
    fun bean2Json(obj: Any): String = INSTANCE.toJson(obj)
}