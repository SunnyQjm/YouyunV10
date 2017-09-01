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
    public static final String IS_ONLY_WIFI_DOWNLOAD = "IS_ONLY_WIFI_DOWNLOAD";
    public static final String IS_ACCEPT_NOTIFY = "IS_ACCEPT_NOTIFY";
    public static final String IS_WIFI_AUTO_UPDATE = "IS_WIFI_AUTO_UPDATE";

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
        if(SpUtils.contains(context, QQ_ACCESS_TOKEN)){
            qqAccessToken = SpUtils.get(context, QQ_ACCESS_TOKEN, "");
        }
        if(SpUtils.contains(context, QQ_EXPIRES_IN)){
            qqExpiresIn = SpUtils.get(context, QQ_EXPIRES_IN, 0L);
        }
        if(SpUtils.contains(context, QQ_OPEN_ID)) {
            qqOpenid = SpUtils.get(context, QQ_OPEN_ID, "");
        }
        if(SpUtils.contains(context, IS_ONLY_WIFI_DOWNLOAD)){
            isOnlyWifiDownload = SpUtils.get(context, IS_ONLY_WIFI_DOWNLOAD, true);
        }
        if(SpUtils.contains(context, IS_ACCEPT_NOTIFY)){
            isAcceptNotify = SpUtils.get(context, IS_ACCEPT_NOTIFY, true);
        }
        if(SpUtils.contains(context, IS_WIFI_AUTO_UPDATE)){
            isWifiAutoUpdate = SpUtils.get(context, IS_WIFI_AUTO_UPDATE, true);
        }
    }

    public void unBind() {
        this.context = null;
    }

    //是否登录
    private static boolean isLogin = false;
    private static String qqAccessToken = "";
    private static String qqOpenid = null;
    private static long qqExpiresIn = 0;
    //登录模式
    private static int loginMode = LOGIN_MODE_NORMAL;
    //Wifi下上传下载
    private static boolean isOnlyWifiDownload = true;
    //是否接收消息推送通知
    private static boolean isAcceptNotify = true;
    //Wifi下自动更新
    private static boolean isWifiAutoUpdate = true;

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

    /**
     * 更新登录模式
     * @param loginMode
     */
    public static void updateLoginMode(int loginMode) {
        YouyunAPI.loginMode = loginMode;
        SpUtils.put(getInstance().getContext(), LOGIN_MODE, loginMode);
    }

    public static void updateIsWifiAutoUpdate(boolean change){
        YouyunAPI.isWifiAutoUpdate = change;
        SpUtils.put(getInstance().getContext(), IS_WIFI_AUTO_UPDATE, change);
    }

    public static void updateIsAcceptNotify(boolean change){
        YouyunAPI.isAcceptNotify = change;
        SpUtils.put(getInstance().getContext(), IS_ACCEPT_NOTIFY, change);
    }

    public static void updateIsOnlyWifiDownload(boolean change){
        YouyunAPI.isOnlyWifiDownload  = change;
        SpUtils.put(getInstance().getContext(), IS_ONLY_WIFI_DOWNLOAD, change);
    }


    public static boolean isIsWifiAutoUpdate() {
        return isWifiAutoUpdate;
    }

    public static boolean isIsOnlyWifiDownload() {
        return isOnlyWifiDownload;
    }

    public static boolean isIsAcceptNotify() {
        return isAcceptNotify;
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
