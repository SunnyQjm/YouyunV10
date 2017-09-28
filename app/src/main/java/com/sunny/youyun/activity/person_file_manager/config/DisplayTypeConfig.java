package com.sunny.youyun.activity.person_file_manager.config;

import com.sunny.youyun.utils.FileTypeUtil;

/**
 * Created by Sunny on 2017/9/25 0025.
 */

public class DisplayTypeConfig {
    public static final int TYPE_DIVIDE_APPLICATION = 0;
    public static final int TYPE_DIVIDE_ZIP = 1;
    public static final int TYPE_DIVIDE_VIDEO = 2;
    public static final int TYPE_DIVIDE_MUSIC = 3;
    public static final int TYPE_DIVIDE_PICTURE = 4;
    public static final int TYPE_DIVIDE_DOCUMENT = 5;
    public static final int TYPE_DIVIDE_HTML = 6;
    public static final int TYPE_DIVIDE_OTHER = 7;

    public static final int PATH_DIVIDE = 11;


    public static String getMIMEByType(int type) {
        switch (type) {
            case TYPE_DIVIDE_APPLICATION:
                return FileTypeUtil.getMyMIME("1.apk");
            case TYPE_DIVIDE_ZIP:
                return FileTypeUtil.getMyMIME("1.zip");
            case TYPE_DIVIDE_VIDEO:
                return FileTypeUtil.getMyMIME("1.mp4");
            case TYPE_DIVIDE_MUSIC:
                return FileTypeUtil.getMyMIME("1.mp3");
            case TYPE_DIVIDE_PICTURE:
                return FileTypeUtil.getMyMIME("1.jpg");
            case TYPE_DIVIDE_DOCUMENT:
                return FileTypeUtil.getMyMIME("1.doc");
            case TYPE_DIVIDE_HTML:
                return FileTypeUtil.getMyMIME("1.html");
            case TYPE_DIVIDE_OTHER:
            default:
                return FileTypeUtil.getMyMIME(null);
        }
    }
}
