package com.sunny.youyun.utils;


import android.support.annotation.NonNull;
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

    private static final String MIME_APPLICATION = "apk";
    private static final String MIME_ZIP = "zip";
    private static final String MIME_VIDEO = "video";
    private static final String MIME_MUSIC = "music";
    private static final String MIME_PICTURE = "img";
    private static final String MIME_DOCUMENT = "doc";
    private static final String MIME_HTML = "html";
    private static final String MIME_OTHER = "other";

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


        return result;
    }

    public static boolean judgeIsVideoPhoto(int resId) {
        return resId == photo || resId == video;
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
        if (fileName == null)
            return type;
        String MIME = getMIME(fileName);
        if (MIME == null || MIME.equals("")) return type;

        type = returnIconIdByStringType(MIME);
        return type;
    }


    public static String getMIME(String fileName) {
        if (fileName == null)
            return null;
        /* 获取文件的后缀名 */
        String end = getSuffix(fileName);
        if (end.equals("")) return "";
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(end);
    }

    /**
     * 获取文件的后缀名
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(@NonNull String fileName) {
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0) {
            return "";
        }
        return fileName.substring(dotIndex + 1, fileName.length()).toLowerCase();
    }

    /**
     * 获取文件的名字，不包含后缀名
     *
     * @return
     */
    public static String getRawName(@NonNull String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, dotIndex);
    }

    /**
     * 为防止文件重名导致的下载问题
     * 进行重命名
     * 举个栗子：滑稽.jpg  ==> 滑稽(1).jpg
     *
     * @param fileName
     * @return
     */
    public static String reName(@NonNull String fileName) {
        String suffix = "." + FileTypeUtil.getSuffix(fileName);
        StringBuilder rawName = new StringBuilder(FileTypeUtil.getRawName(fileName));
        int start = rawName.lastIndexOf("(");
        int end = rawName.lastIndexOf(")");
        int num = 1;
        if (start < 0 || end < 0 || start >= end || end != rawName.length() - 1) {
            rawName.append("(1)");
            return rawName + suffix;
        }
        try {
            num = Integer.valueOf(rawName.substring(start + 1, end));
        } catch (Exception e) {
            e.printStackTrace();
            rawName.append("(1)");
            return rawName + suffix;
        }
        rawName.replace(start + 1, end, String.valueOf(num + 1));
        return rawName + suffix;
    }


    public static String getMyMIME(String fileName) {
        String type = getMIME(fileName);
        if (type == null)
            return MIME_OTHER;
        if (type.startsWith("image/")) {
            return MIME_PICTURE;
        }
        if (type.startsWith("audio")) {
            return MIME_MUSIC;
        }
        if (type.startsWith("video")) {
            return MIME_VIDEO;
        }
        switch (type) {
            case "application/zip":
                return MIME_ZIP;
            case "application/msword":
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
            case "application/vnd.ms-powerpoint":
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
            case "application/vnd.ms-excel":
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
            case "application/pdf":
                return MIME_DOCUMENT;
            case "application/vnd.android.package-archive":
                return MIME_APPLICATION;
            default:
                return MIME_OTHER;
        }
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
