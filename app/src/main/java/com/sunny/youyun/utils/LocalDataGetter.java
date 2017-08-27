package com.sunny.youyun.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.activity.file_manager.item.AppInfoItem;
import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.activity.file_manager.model.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qjm3662 on 2016/11/2 0002.
 */

public class LocalDataGetter {

    public interface GetDataCallback<T> {
        void onSuccess(T t);

        void onFail(String msg);
    }

    public static void getAppInfosIgnoreSystemAppSync(final Application application,
                                                      GetDataCallback<List<AppInfoItem>> callback) {
        MyThreadPool.getInstance()
                .submit(() -> callback.onSuccess(getAppInfosIgnoreSystemApp(application)));
    }

    /**
     * This method list all app installed, except system app.
     *
     * @param application application
     * @return the list of AppInfo
     */
    public static List<AppInfoItem> getAppInfosIgnoreSystemApp(final Application application) {
        if (application == null)
            return Collections.emptyList();
        PackageManager pm = application.getPackageManager();
        List<PackageInfo> p_list = pm.getInstalledPackages(0);
        List<AppInfoItem> list = new ArrayList<>();
        for (PackageInfo p :
                p_list) {
            ApplicationInfo applicationInfo = p.applicationInfo;
            if (applicationInfo.sourceDir.contains("system"))
                continue;
            File file = new File(applicationInfo.sourceDir);
            if (!file.exists())
                continue;
            list.add(new AppInfoItem(new AppInfo.Builder()
                    .appName(String.valueOf(applicationInfo.loadLabel(pm)))
                    .size(file.length())
                    .lastModified(file.lastModified())
                    .icon(applicationInfo.loadIcon(pm))
                    .path(applicationInfo.sourceDir)));
        }
        return list;
    }


    public static void getDocumentSync(final Context context, GetDataCallback<Map<String, List<FileItem>>> callback) {
        MyThreadPool.getInstance()
                .submit(() -> {
                    final Map<String, List<FileItem>> mGroupMap = new HashMap<>();
                    Uri mDocumentUri = MediaStore.Files.getContentUri("external");
                    ContentResolver mContentResolver = context.getContentResolver();
                    //相当于我们常用sql where 后面的写法
                    String selection = MediaStore.Files.FileColumns.MIME_TYPE + "= ? "
                            + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                            + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                            + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                            + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                            + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                            + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                            + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? ";


                    Cursor mCursor = mContentResolver.query(mDocumentUri, null, selection,
                            new String[]{"application/msword", "application/pdf", "text/plain",
                                    "application/vnd.ms-powerpoint", "application/vnd.ms-excel",
                                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                                    "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
                            MediaStore.Files.FileColumns.DATE_MODIFIED);
                    if (mCursor == null) {
                        callback.onFail("mCursor is null");
                        Logger.e("mCursor is null");
                        return;
                    }

                    while (mCursor.moveToNext()) {
                        //获取视频的路径
                        String path = mCursor.getString(mCursor
                                .getColumnIndex(MediaStore.Files.FileColumns.DATA));
                        //获取该视频的父路径名
                        String parentName = new File(path).getParentFile().getName();

                        //根据父路径名将视频放入到mGruopMap中
                        if (!mGroupMap.containsKey(parentName)) {
                            List<FileItem> chileList = new ArrayList<>();
                            File file = new File(path);
                            if (!file.exists()) {
                                Logger.e("文件不存在: " + file);
                            }

                            chileList.add(new FileItem(new FileItem.Builder()
                                    .size(file.length())
                                    .name(file.getName())
                                    .lastModifiedTime(file.lastModified())
                                    .path(file.getPath())));
                            mGroupMap.put(parentName, chileList);
                        } else {
                            File file = new File(path);
                            mGroupMap.get(parentName).add(new FileItem(new FileItem.Builder()
                                    .size(file.length())
                                    .name(file.getName())
                                    .lastModifiedTime(file.lastModified())
                                    .path(file.getPath())));
                        }
                    }

                    mCursor.close();
                    for (List<FileItem> element : mGroupMap.values())
                        Collections.reverse(element);
                    callback.onSuccess(mGroupMap);
                });
    }

    public static Map<String, List<FileItem>> getAudio(final Context context) {
        final Map<String, List<FileItem>> mGroupMap = new HashMap<>();
        Uri mAudioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolve = context.getContentResolver();
        Cursor mCursor = mContentResolve.query(mAudioUri, null, null, null,
                MediaStore.Audio.Media.DATE_MODIFIED);
        if (mCursor == null) {
            Logger.e("mCursor is null");
            return Collections.emptyMap();
        }

        while (mCursor.moveToNext()) {
            //获取音频的路径
            String path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            //获取该音频的父路径名
            String parentName = new File(path).getParentFile().getName();

            //根据父路径名将音频放入到mGruopMap中
            if (!mGroupMap.containsKey(parentName)) {
                List<FileItem> chileList = new ArrayList<>();
                File file = new File(path);
                if (!file.exists()) {
                    Logger.e("文件不存在: " + file);
                }

                chileList.add(new FileItem(new FileItem.Builder()
                        .size(file.length())
                        .name(file.getName())
                        .lastModifiedTime(file.lastModified())
                        .path(file.getPath())));
                mGroupMap.put(parentName, chileList);
            } else {
                File file = new File(path);
                mGroupMap.get(parentName).add(new FileItem(new FileItem.Builder()
                        .size(file.length())
                        .name(file.getName())
                        .lastModifiedTime(file.lastModified())
                        .path(file.getPath())));
            }
        }
        mCursor.close();
        for (List<FileItem> element : mGroupMap.values())
            Collections.reverse(element);
        return mGroupMap;
    }

    public static Map<String, List<FileItem>> getVideo(final Context context,
                                                       GetDataCallback<Map<String, List<FileItem>>> callback) {
        final Map<String, List<FileItem>> mGroupMap = new HashMap<>();
        Uri mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();
        Cursor mCursor = mContentResolver.query(mVideoUri, null, null, null,
                MediaStore.Video.Media.DATE_MODIFIED);
        if (mCursor == null) {
            if (callback != null)
                callback.onFail("mCursor is null");
            Logger.e("mCursor is null");
            return null;
        }

        while (mCursor.moveToNext()) {
            //获取视频的路径
            String path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Video.Media.DATA));
            //获取该视频的父路径名
            String parentName = new File(path).getParentFile().getName();

            //根据父路径名将视频放入到mGruopMap中
            if (!mGroupMap.containsKey(parentName)) {
                List<FileItem> chileList = new ArrayList<>();
                File file = new File(path);
                if (!file.exists()) {
                    Logger.e("文件不存在: " + file);
                }

                chileList.add(new FileItem(new FileItem.Builder()
                        .size(file.length())
                        .name(file.getName())
                        .lastModifiedTime(file.lastModified())
                        .path(file.getPath())));
                mGroupMap.put(parentName, chileList);
            } else {
                File file = new File(path);
                mGroupMap.get(parentName).add(new FileItem(new FileItem.Builder()
                        .size(file.length())
                        .name(file.getName())
                        .lastModifiedTime(file.lastModified())
                        .path(file.getPath())));
            }
        }
        mCursor.close();
        for (List<FileItem> element : mGroupMap.values())
            Collections.reverse(element);
        mGroupMap.putAll(getAudio(context));
        if (callback != null) {
            callback.onSuccess(mGroupMap);
        }
        return mGroupMap;
    }

    /**
     * 利用ContentProvider扫描手机中的视频和音频，此方法在运行在子线程中
     */
    public static void getVideoSync(final Context context, GetDataCallback<Map<String, List<FileItem>>> callback) {
        MyThreadPool.getInstance()
                .submit(() -> {
                    final Map<String, List<FileItem>> mGroupMap = new HashMap<>();
                    Uri mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    ContentResolver mContentResolver = context.getContentResolver();
                    Cursor mCursor = mContentResolver.query(mVideoUri, null, null, null,
                            MediaStore.Video.Media.DATE_MODIFIED);
                    if (mCursor == null) {
                        callback.onFail("mCursor is null");
                        Logger.e("mCursor is null");
                        return;
                    }

                    while (mCursor.moveToNext()) {
                        //获取视频的路径
                        String path = mCursor.getString(mCursor
                                .getColumnIndex(MediaStore.Video.Media.DATA));
                        //获取该视频的父路径名
                        String parentName = new File(path).getParentFile().getName();

                        //根据父路径名将视频放入到mGruopMap中
                        if (!mGroupMap.containsKey(parentName)) {
                            List<FileItem> chileList = new ArrayList<>();
                            File file = new File(path);
                            if (!file.exists()) {
                                Logger.e("文件不存在: " + file);
                            }

                            chileList.add(new FileItem(new FileItem.Builder()
                                    .size(file.length())
                                    .name(file.getName())
                                    .lastModifiedTime(file.lastModified())
                                    .path(file.getPath())));
                            mGroupMap.put(parentName, chileList);
                        } else {
                            File file = new File(path);
                            mGroupMap.get(parentName).add(new FileItem(new FileItem.Builder()
                                    .size(file.length())
                                    .name(file.getName())
                                    .lastModifiedTime(file.lastModified())
                                    .path(file.getPath())));
                        }
                    }
                    mCursor.close();
                    for (List<FileItem> element : mGroupMap.values())
                        Collections.reverse(element);
                    mGroupMap.putAll(getAudio(context));
                    callback.onSuccess(mGroupMap);
                });
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    public static void getPictureSync(final Context context, GetDataCallback<Map<String, List<FileItem>>> callback) {
        MyThreadPool.getInstance().submit(() -> {
                    final Map<String, List<FileItem>> mGroupMap = new HashMap<>();
                    Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    ContentResolver mContentResolver = context.getContentResolver();
                    //只查询jpeg和png的图片
                    Cursor mCursor = mContentResolver.query(mImageUri, null,
                            MediaStore.Images.Media.MIME_TYPE + "=? or "
                                    + MediaStore.Images.Media.MIME_TYPE + "=?",
                            new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                    if (mCursor == null) {
                        callback.onFail("mCursor is null");
                        Logger.e("mCursor is null");
                        return;
                    }
                    while (mCursor.moveToNext()) {
                        //获取图片的路径
                        String path = mCursor.getString(mCursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                        //获取该图片的父路径名
                        String parentName = new File(path).getParentFile().getName();

                        //根据父路径名将图片放入到mGruopMap中
                        if (!mGroupMap.containsKey(parentName)) {
                            List<FileItem> chileList = new ArrayList<>();
                            File file = new File(path);
                            if (!file.exists()) {
                                Logger.e("文件不存在: " + file);
                            }

                            chileList.add(new FileItem(new FileItem.Builder()
                                    .size(file.length())
                                    .name(file.getName())
                                    .lastModifiedTime(file.lastModified())
                                    .path(file.getPath())));
                            mGroupMap.put(parentName, chileList);
                        } else {
                            File file = new File(path);
                            mGroupMap.get(parentName).add(new FileItem(new FileItem.Builder()
                                    .size(file.length())
                                    .name(file.getName())
                                    .lastModifiedTime(file.lastModified())
                                    .path(file.getPath())));
                        }
                    }
                    mCursor.close();
                    for (List<FileItem> element : mGroupMap.values())
                        Collections.reverse(element);
                    callback.onSuccess(mGroupMap);
                }
        );
    }
}
