package com.sunny.youyun.wifidirect.utils;

import com.google.gson.Gson;

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
}
