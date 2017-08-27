package com.sunny.youyun.model;

import android.content.Context;

import com.sunny.youyun.utils.SpUtils;

/**
 * Created by Sunny on 2017/8/16 0016.
 */

public enum  YouyunAPI {
    INSTANCE;
    public static YouyunAPI getInstance(){
        return INSTANCE;
    }

    public static final String IS_LOGIN = "IS_LOGIN";

    private Context context = null;

    public void bind(Context context){
        this.context = context;
    }

    public void unBind(){
        this.context = null;
    }

    private static boolean isLogin = false;


    public static void updateIsLogin(boolean isLogin){
        YouyunAPI.isLogin = isLogin;

        SpUtils.put(getInstance().getContext(), IS_LOGIN, isLogin);
    }

    public Context getContext() {
        return context;
    }
}
