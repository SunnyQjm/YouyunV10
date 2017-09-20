package com.sunny.youyun.internet.api;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class ApiInfo {
    //    public static final String IP = "http://123.206.80.54";
//    public static final String IP = "http://123.207.96.66";
//    public static final String IP = "http://192.168.1.3";
    private static final String IP = "http://192.168.1.101";
    //    private static final String IP = "http://172.6.0.186";
//    private static final String IP = "http://192.168.10.176";
    public static final int port = 8080;
    public static final String BaseUrl = IP + ":" + port + "/";
//    public static final String BaseUrl = IP + "/";

    public static final String DOWNLOAD = "file/download/",
            BASE_DOWNLOAD_URL = BaseUrl + DOWNLOAD;
    public static final String GET_FILE_INFO = "file/message", GET_FILE_INFO_IDENTIFY_CODE = "identifycode";


    ///////////////////////////////////////////////////////
    ///// UserService
    //////////////////////////////////////////////////////
    public static final String LOGIN_URL = "login",
            LOGIN_USERNAME = "phone", LOGIN_PASSWORD = "password", LOGIN_TYPE = "type";

    public static final int LOGIN_TY_QQ = 1, LOGIN_TYPE_WE_CHAT = 2;
    //QQ一键登录
    public static final String QQ_LOGIN_URL = "otherLogin",
            QQ_LOGIN_ACCESS_TOKEN = "access_token", QQ_LOGIN_OPEN_ID = "openId";

    //发送验证码
    public static final String SEND_CODE = "sendCode";

    //注册
    public static final String REGISTER = "register";

    //修改头像
    public static final String MODIFY_AVATAR = "authc/user/setAvatar",
            MODIFY_AVATAR_AVATAR = "avatar";

    public static final String LOGOUT = "authc/logout";

    public static final String GET_USER_INFO = "authc/user";


    ///////////////////////////////////////////////////////////
    ////////FileService
    ///////////////////////////////////////////////////////////
    public static final String UPLOAD_FILE_URL = "file/upload", UPLOAD_FILE_PARAM_SHARE = "share",
            UPLOAD_FILE_PARAM_LEFT_ALLOW_DOWNLOAD_COUNT = "leftAllowDownloadCount",
            UPLOAD_FILE_PARAM_EXPIRE_TIME = "expireTime", UPLOAD_FILE_PARAM_SCORE = "score",
            UPLOAD_FILE_PARAM_IS_PRIVATE = "privateOwn", UPLOAD_FILE_PARAM_PARENT_PATH = "test.txt",
            UPLOAD_FILE_PARAM_DESCRIPTION = "description", UPLOAD_FILE_PARAM_MD5 = "md5",
            UPLOAD_FILE_PARAM_SIZE = "size", UPLOAD_FILE_PARAM_NAME = "name",
            UPLOAD_FILE_PARAM_MIME = "MIME";
    public static final String UPLOAD_FILE_CHECK_URL = "file/uploadCheck";


    ///////////////////////////////////////////////////////////
    /////////ForumService
    //////////////////////////////////////////////////////////
    public static final String GET_FORUM_URL = "forum/files", GET_FORUM_PAGE = "page",
            GET_FORUM_SIZE = "size", GET_FORUM_SORT_BY_DATE = "createTime",
            GET_FORUM_SORT_BY_DOWNLOAD_COUNT = "downloadCount";
    public static final int GET_FORUM_DEFAULT_SIZE = 10;

    public static final String ADD_COMMENT_URL = "authc/forum/comment",
            ADD_COMMENT_FILE_ID = "fileId", ADD_COMMENT_CONTENT = "content";

    public static final String GET_COMMENTS_URL = "forum/get/comment",
            GET_COMMENTS_FILE_ID = "fileId";
    public static final String STAR_URL = "authc/forum/star ",
            STAR_FILE_ID = "fileId";
}
