package com.sunny.youyun.utils;


import android.webkit.MimeTypeMap;

import com.sunny.youyun.R;


/**
 * Created by Sunny on 2017/5/13 0013.
 */

public class FileTypeUtil {

    private static final int direct = R.drawable.directy;
    private static final int music = R.drawable.music;
    private static final int file = R.drawable.file;
    private static final int video = R.drawable.vedio;
    private static final int photo = R.drawable.picture;
    private static final int zip = R.drawable.zip;
    private static final int word = R.drawable.word;
    private static final int ppt = R.drawable.ppt;
    private static final int pdf = R.drawable.pdf;
    private static final int excel = R.drawable.excel;
    private static final int code = R.drawable.code;
    private static final int apk = R.drawable.apk;


    public static int getVideo() {
        return video;
    }

    public static int getPhoto() {
        return photo;
    }

    public static int getDirect() {
        return direct;
    }

    public static int getIconByFileNameWithoutVideoPhoto(String finaName) {
        int result = getIconIdByFileName(finaName);

        if (result == photo || result == video) {
            return -1;
        }
        return result;
    }

    public static int getApk() {
        return apk;
    }

    /**
     * 通过文件名获取icon的id
     *
     * @param fileName
     * @return
     */
    public static int getIconIdByFileName(String fileName) {
        int type = file;
        if(fileName == null)
            return type;
        String MIME = getMIME(fileName);
        if (MIME == null || MIME.equals("")) return type;

        type = returnIconIdByStringType(MIME);
        return type;
    }


    public static String getMIME(String fileName){
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0) {
            return "";
        }
        /* 获取文件的后缀名 */
        String end = fileName.substring(dotIndex + 1, fileName.length()).toLowerCase();
        if (end.equals("")) return "";
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(end);
    }


    private static int returnIconIdByStringType(String type) {
        if (type == null)
            return file;
        if (type.startsWith("image/")) {
            return photo;
        }
        if (type.startsWith("audio")) {
            return music;
        }
        if (type.startsWith("video")) {
            return video;
        }
        switch (type) {
            case "application/zip":
                return zip;
            case "application/msword":
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return word;
            case "application/vnd.ms-powerpoint":
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                return ppt;
            case "application/vnd.ms-excel":
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                return excel;
            case "application/pdf":
                return pdf;
            case "application/vnd.android.package-archive":
                return apk;
            default:
                return file;
        }
    }
}
