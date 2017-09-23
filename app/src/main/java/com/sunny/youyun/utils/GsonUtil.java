package com.sunny.youyun.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;

/**
 * Created by Sunny on 2017/4/19 0019.
 */
public class GsonUtil {
    public static Gson instance = null;

    public static Gson getInstance() {
        if(instance == null){
            instance = new Gson();
        }
        return instance;
    }

    public static <T> T json2Bean(String json, Class<T> beanClass){
        T bean = null;
        try {
            bean = getInstance().fromJson(json, beanClass);
        } catch (JsonSyntaxException e) {
            Logger.i("json 解析异常：" + json);
            e.printStackTrace();
        }
        return bean;
    }

    public static String bean2Json(Object object){
        return getInstance().toJson(object);
    }
}
