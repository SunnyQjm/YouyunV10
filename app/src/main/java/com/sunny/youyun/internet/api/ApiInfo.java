package com.sunny.youyun.internet.api;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class ApiInfo {
//    public static final String IP = "http://123.206.80.54";
//    public static final String IP = "http://123.207.96.66";
    public static final String IP = "http://192.168.1.3";
    public static final int port = 8080;
    public static final String BaseUrl = IP + ":" + port + "/";
//    public static final String BaseUrl = IP + "/";

    public static final String DOWNLOAD = "file/download/",
            BASE_DOWNLOAD_URL = BaseUrl + DOWNLOAD;
    public static final String GET_FILE_INFO = "file/message", GET_FILE_INFO_IDENTIFY_CODE = "identifycode";

    public static final String LOGIN_URL = "login",
            LOGIN_USERNAME = "phone", LOGIN_PASSWORD = "password";
}
