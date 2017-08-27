package com.sunny.youyun.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sunny on 2017/8/8 0008.
 */

public class JsonUtil {
    public static String convertToJsonString(String[] keys, Object[] os){
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < keys.length; i++) {
                jsonObject.put(keys[i], os[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
