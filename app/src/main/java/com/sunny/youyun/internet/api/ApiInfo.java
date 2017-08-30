package com.sunny.youyun.internet.api;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class ApiInfo {
    //    public static final String IP = "http://123.206.80.54";
//    public static final String IP = "http://123.207.96.66";
    public static final String IP = "http://192.168.1.3";
    public static final int port = 8080;
    public static final String BaseUrl = IP + ":" + port + "/youyun/";
//    public static final String BaseUrl = IP + "/";

    public static final String DOWNLOAD = "file/download/",
            BASE_DOWNLOAD_URL = BaseUrl + DOWNLOAD;
    public static final String GET_FILE_INFO = "file/message", GET_FILE_INFO_IDENTIFY_CODE = "identifycode";

    public static final String LOGIN_URL = "login",
            LOGIN_USERNAME = "phone", LOGIN_PASSWORD = "password";

    //QQ一键登录
    public static final String QQ_LOGIN_URL = "otherLogin",
            QQ_LOGIN_ACCESS_TOKEN = "token", QQ_LOGIN_OPEN_ID = "openId";

    //发送验证码
    public static final String SEND_CODE = "sendCode";

    //注册
    public static final String REGISTER = "register";

    //修改头像
    public static final String MODIFY_AVATAR = "authc/user/setAvatar",
            MODIFY_AVATAR_AVATAR = "avatar";


}
