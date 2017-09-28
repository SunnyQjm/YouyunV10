package com.sunny.youyun.internet.api;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class ApiInfo {
    //    public static final String IP = "http://123.206.80.54";
//    public static final String IP = "http://123.207.96.66";
//    public static final String IP = "http://192.168.1.3";
    private static final String IP = "http://192.168.1.104";
    //    private static final String IP = "http://172.6.0.186";
//    private static final String IP = "http://192.168.10.176";
    public static final int port = 8080;
    public static final String BaseUrl = IP + ":" + port + "/";
//    public static final String BaseUrl = IP + "/";

    public static final String DOWNLOAD = "file/download/",
            BASE_DOWNLOAD_URL = BaseUrl + DOWNLOAD;
    public static final String GET_FILE_INFO = "file/message", GET_FILE_INFO_IDENTIFY_CODE = "identifycode";


    ////////////////////////////////////////////////////////
    /////Status Code
    ///////////////////////////////////////////////////////

    //未登录错误
    public static final int STATUS_CODE_NOT_LOGIN = -1;
    //参数缺失
    public static final int STATUS_CODE_PARAM_NOT_COMPLETE = -2;
    //登录失效
    public static final int STATUS_CODE_LOGIN_INVALID = -3;


    ///////////////////////////////////////////////////////
    ///// UserService
    //////////////////////////////////////////////////////
    //登录
    public static final String LOGIN_URL = "login",
            LOGIN_USERNAME = "phone", LOGIN_PASSWORD = "password", LOGIN_TYPE = "type";

    public static final int LOGIN_TY_QQ = 1, LOGIN_TYPE_WE_CHAT = 2;
    //QQ一键登录
    public static final String QQ_LOGIN_URL = "otherLogin",
            QQ_LOGIN_ACCESS_TOKEN = "access_token", QQ_LOGIN_OPEN_ID = "openId";

    //发送验证码
    public static final String SEND_CODE = "sendCode";

    //注册
    public static final String REGISTER_URL = "register", REGISTER_PHONE = "phone",
            REGISTER_NICKNAME = "username", REGISTER_PASSWORD = "password",
            REGISTER_CODE = "code";

    //修改头像
    public static final String MODIFY_AVATAR = "authc/user/setAvatar",
            MODIFY_AVATAR_AVATAR = "avatar";

    //登出
    public static final String LOGOUT = "authc/logout";

    //获取用户信息
    public static final String GET_USER_INFO = "authc/user";

    //获取用户动态
    public static final String GET_USER_DYNAMIC_URL = "authc/file/event",
            GET_USER_DYNAMIC_PAGE = "page", GET_USER_DYNAMIC_SIZE = "size",
            GET_USER_DYNAMIC_TYPE = "type";

    //分类获取用户上传的文件
    public static final String GET_USER_FILE_BY_TYPE_URL = "authc/file/type",
            GET_USER_FILE_BY_TYPE_MIME = "MIME", GET_USER_FILE_BY_TYPE_PAGE = "page",
            GET_USER_FILE_BY_TYPE_SIZE = "size";


    ///////////////////////////////////////////////////////////
    ////////FileService
    ///////////////////////////////////////////////////////////

    //上传
    public static final String UPLOAD_FILE_URL = "file/upload", UPLOAD_FILE_PARAM_SHARE = "share",
            UPLOAD_FILE_PARAM_LEFT_ALLOW_DOWNLOAD_COUNT = "leftAllowDownloadCount",
            UPLOAD_FILE_PARAM_EXPIRE_TIME = "expireTime", UPLOAD_FILE_PARAM_SCORE = "score",
            UPLOAD_FILE_PARAM_IS_PRIVATE = "privateOwn", UPLOAD_FILE_PARAM_PARENT_PATH = "test.txt",
            UPLOAD_FILE_PARAM_DESCRIPTION = "description", UPLOAD_FILE_PARAM_MD5 = "md5",
            UPLOAD_FILE_PARAM_SIZE = "size", UPLOAD_FILE_PARAM_NAME = "name",
            UPLOAD_FILE_PARAM_MIME = "MIME";
    //上传冗余检测
    public static final String UPLOAD_FILE_CHECK_URL = "file/uploadCheck";
    //收藏文件
    public static final String FILE_COLLECT_URL = "authc/file/store", FILE_COLLECT_FILE_ID = "fileId",
            FILE_COLLECT_TYPE = "type";

    /**
     * 5.获取路径信息 method: get /authc/file/dir
     {
     parentId: //当为null时返回根目录/下的所有文件
     }
     */
    public static final String GET_UPLOAD_FILES_URL = "authc/file/dir",
            GET_UPLOAD_FILES_PARENT_ID = "parentId", GET_UPLOAD_FILES_ROOT_PATH = "/";


    public static final String CREATE_DIRECTORY_URL = "authc/file/createDirectory",
            CREATE_DIRECTORY_PARENT_ID = "parentId", CREATE_DIRECTORY_NAME = "name";

    ///////////////////////////////////////////////////////////
    /////////ForumService
    //////////////////////////////////////////////////////////
    public static final String GET_FORUM_URL = "forum/files", GET_FORUM_PAGE = "page",
            GET_FORUM_SIZE = "size", GET_FORUM_SORT_BY_DATE = "createTime",
            GET_FORUM_SORT_BY_DOWNLOAD_COUNT = "downloadCount";
    public static final int GET_FORUM_DEFAULT_SIZE = 5;

    public static final String ADD_COMMENT_URL = "authc/forum/comment",
            ADD_COMMENT_FILE_ID = "fileId", ADD_COMMENT_CONTENT = "content";

    public static final String GET_COMMENTS_URL = "forum/get/comment",
            GET_COMMENTS_FILE_ID = "fileId";
    public static final String STAR_URL = "authc/forum/star ",
            STAR_FILE_ID = "fileId";


    ////////////////////////////////////////////////////////////////////////////
    /////////////TokenService
    ///////////////////////////////////////////////////////////////////////////
    public static final String UPDATE_COOKIE_BY_TOKEN_URL = "updateToken",
            UPDATE_COOKIE_BY_TOKEN_TOKEN = "token";



    ///////////////////////////////////////////////////////////////////////////
    ////////////MediaType
    //////////////////////////////////////////////////////////////////////////

    public static final String MEDIA_TYPE_JSON = "Application/json; charset=utf-8";
}
