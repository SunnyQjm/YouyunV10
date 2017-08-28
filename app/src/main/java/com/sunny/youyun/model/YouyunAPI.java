package com.sunny.youyun.model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.sunny.youyun.utils.SpUtils;

/**
 * Created by Sunny on 2017/8/16 0016.
 */

public enum YouyunAPI {
    @SuppressLint("StaticFieldLeak")
    INSTANCE;

    public static YouyunAPI getInstance() {
        return INSTANCE;
    }

    public static final String IS_LOGIN = "IS_LOGIN";

    public static final String QQ_ACCESS_TOKEN = "QQ_ACCESS_TOKEN";
    public static final String QQ_OPEN_ID = "QQ_OPEN_ID";
    public static final String QQ_EXPIRES_IN = "QQ_EXPIRES_IN";

    public static final String LOGIN_MODE = "LOGIN_MODE";

    public static final int LOGIN_MODE_NORMAL = 0;
    public static final int LOGIN_MODE_QQ = 1;

    private Context context = null;

    public void bind(Context context) {
        this.context = context;
        if (SpUtils.contains(context, IS_LOGIN)) {
            isLogin = SpUtils.get(context, IS_LOGIN, false);
        }
        if (SpUtils.contains(context, LOGIN_MODE)) {
            loginMode = SpUtils.get(context, LOGIN_MODE, LOGIN_MODE_NORMAL);
        }
    }

    public void unBind() {
        this.context = null;
    }

    private static boolean isLogin = false;
    private static String qqAccessToken = "";
    private static String qqOpenid = null;
    private static long qqExpiresIn = 0;
    private static int loginMode = LOGIN_MODE_NORMAL;

    public static void updateIsLogin(boolean isLogin) {
        YouyunAPI.isLogin = isLogin;
        SpUtils.put(getInstance().getContext(), IS_LOGIN, isLogin);
    }

    public static void updateQQLoginResult(QQLoginResult result) {
        YouyunAPI.qqAccessToken = result.getAccessToken();
        YouyunAPI.qqOpenid = result.getOpenid();
        YouyunAPI.qqExpiresIn = result.getExpiresIn();
        SpUtils.put(getInstance().getContext(), QQ_ACCESS_TOKEN, result.getAccessToken());
        SpUtils.put(getInstance().getContext(), QQ_OPEN_ID, result.getOpenid());
        SpUtils.put(getInstance().getContext(), QQ_EXPIRES_IN, result.getExpiresIn());
    }

    public static void updateLoginMode(int loginMode) {
        YouyunAPI.loginMode = loginMode;
        SpUtils.put(getInstance().getContext(), LOGIN_MODE, loginMode);
    }

    public static boolean isIsLogin() {
        return isLogin;
    }

    public static String getOpenId() {
        return qqOpenid;
    }

    public static String getAccessToken(){
        return qqAccessToken;
    }

    public static long getExpiresIn(){
        return qqExpiresIn;
    }

    public static int getLoginMode() {
        return loginMode;
    }

    public Context getContext() {
        return context;
    }
}
