package com.sunny.youyun.internet.upload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.App;
import com.sunny.youyun.activity.file_manager.config.FileManagerRequest;
import com.sunny.youyun.activity.upload_setting.UploadSettingActivity;
import com.sunny.youyun.internet.upload.config.UploadConfig;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.RouterUtils;

import java.io.File;

import static com.sunny.youyun.internet.upload.FileUploadService.PARAM_ALLOW_DOWN_COUNT;
import static com.sunny.youyun.internet.upload.FileUploadService.PARAM_DESCRIPTION;
import static com.sunny.youyun.internet.upload.FileUploadService.PARAM_EXPIRE_TIME;
import static com.sunny.youyun.internet.upload.FileUploadService.PARAM_FILE_PATH;
import static com.sunny.youyun.internet.upload.FileUploadService.PARAM_IS_PRIVATE;
import static com.sunny.youyun.internet.upload.FileUploadService.PARAM_IS_SHARE;
import static com.sunny.youyun.internet.upload.FileUploadService.PARAM_PARENT_ID;
import static com.sunny.youyun.internet.upload.FileUploadService.PARAM_POSITION;
import static com.sunny.youyun.internet.upload.FileUploadService.PARAM_SCORE;

/**
 *
 * Created by Sunny on 2017/8/24 0024.
 */

public enum FileUploader {
    @SuppressLint("StaticFieldLeak")INSTANCE;
    public static FileUploader getInstance(){
        return INSTANCE;
    }

    private Context context;
    public static void bind(Context context){
        getInstance().context = context;
    }

    public static void unBind(){
        getInstance().context = null;
    }



    public static final int PATH_S = 234;
    private static final int UPLOAD_SETTING = 235;

    public static void dealUploadResult(Activity activity, int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case PATH_S:        //选择文件的结果
                if (data == null) {
                    Logger.i("PATH_S: data is null");
                    return;
                }
                String[] paths = data.getStringArrayExtra(FileManagerRequest.KEY_PATH);
                Intent intent = new Intent(activity, UploadSettingActivity.class);
                intent.putExtra(FileManagerRequest.KEY_PATH, paths);
                intent.putExtra(FileManagerRequest.KEY_PATH_NAME,
                        data.getStringExtra(FileManagerRequest.KEY_PATH_NAME));
                intent.putExtra(FileManagerRequest.KEY_PATH_ID,
                        data.getStringExtra(FileManagerRequest.KEY_PATH_ID));
                RouterUtils.openForResult(activity, intent, UPLOAD_SETTING);
                break;
            case UPLOAD_SETTING:
                if (data == null) {
                    Logger.i("UPLOAD_SETTING: data is null");
                    return;
                }
                paths = data.getStringArrayExtra(UploadConfig.PATH);
                int allowDownloadCount = data.getIntExtra(UploadConfig.ALLOW_DOWNLOAD_COUNT, -1);
                long expireTime = data.getLongExtra(UploadConfig.EFFECT_DATE, -1);
                boolean isPublic = data.getBooleanExtra(UploadConfig.IS_PUBLIC, true);
                int score = data.getIntExtra(UploadConfig.DOWNLOAD_SCORE, 0);
                String parentId = data.getStringExtra(UploadConfig.PARENT_ID);
                String description = data.getStringExtra(UploadConfig.DESCRIPTION);
                for (String path : paths) {
                    FileUploader.getInstance()
                            .upload(new FileUploadFileParam
                                    .Builder()
                                    .filePath(path)
                                    .allowDownCount(allowDownloadCount)
                                    .expireTime(expireTime)
                                    .isPrivate(!isPublic)
                                    .isShare(isPublic)
                                    .parentId(parentId)
                                    .description(description)
                                    .score(score)
                                    .build());
                }
                break;
        }
    }

    public static void dealUploadResult(Fragment fragment, int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case PATH_S:        //选择文件的结果
                if (data == null) {
                    Logger.i("PATH_S: data is null");
                    return;
                }
                String[] paths = data.getStringArrayExtra(FileManagerRequest.KEY_PATH);
                Intent intent = new Intent(fragment.getContext(), UploadSettingActivity.class);
                intent.putExtra(FileManagerRequest.KEY_PATH, paths);
                intent.putExtra(FileManagerRequest.KEY_PATH_NAME,
                        data.getStringArrayExtra(FileManagerRequest.KEY_PATH_NAME));
                intent.putExtra(FileManagerRequest.KEY_PATH_ID,
                        data.getStringArrayExtra(FileManagerRequest.KEY_PATH_ID));
                RouterUtils.openForResult(fragment, intent, UPLOAD_SETTING);
                break;
            case UPLOAD_SETTING:
                if (data == null) {
                    Logger.i("UPLOAD_SETTING: data is null");
                    return;
                }
                paths = data.getStringArrayExtra(UploadConfig.PATH);
                int allowDownloadCount = data.getIntExtra(UploadConfig.ALLOW_DOWNLOAD_COUNT, -1);
                long expireTime = data.getLongExtra(UploadConfig.EFFECT_DATE, -1);
                boolean isPublic = data.getBooleanExtra(UploadConfig.IS_PUBLIC, true);
                int score = data.getIntExtra(UploadConfig.DOWNLOAD_SCORE, 0);
                String parentId = data.getStringExtra(UploadConfig.PARENT_ID);
                String description = data.getStringExtra(UploadConfig.DESCRIPTION);
                for (String path : paths) {
                    FileUploader.getInstance()
                            .upload(new FileUploadFileParam
                                    .Builder()
                                    .filePath(path)
                                    .allowDownCount(allowDownloadCount)
                                    .expireTime(expireTime)
                                    .isPrivate(!isPublic)
                                    .isShare(isPublic)
                                    .parentId(parentId)
                                    .description(description)
                                    .score(score)
                                    .build());
                }
                break;
        }
    }


    /**
     * 开始上传
     * @param uploadFileParam
     */
    public void upload(FileUploadFileParam uploadFileParam){
        if (uploadFileParam.getFilePath() == null)
            return;

        final File f = new File(uploadFileParam.getFilePath());
        if (!f.exists()) {
            Logger.e("文件不存在");
            return;
        }

        String name = f.getName();
        //添加即将上传的文件信息
        final InternetFile internetFile = new InternetFile.Builder()
                .name(name)
                .size(f.length())
                .fileTAG(InternetFile.TAG_UPLOAD)
                .path(f.getPath())
                .status(InternetFile.Status.DOWNLOADING)
                .createTime(System.currentTimeMillis())
                .build();

        int position;
        //添加文件和获取该文件的位置是一个原子操作
        synchronized (App.mList_UploadRecord) {
            position = App.mList_UploadRecord.size();
            internetFile.setPosition(position);
            App.mList_UploadRecord.add(internetFile);
        }
        if(context == null)
            return;
        action2Upload(uploadFileParam, position, FileUploadService.ACTION_UPLOAD);
    }

    /**
     * 暂停上传
     * @param path
     * @param position
     */
    public void pause(String path, int position){
        if(context == null)
            return;
        action2Service(path, position, FileUploadService.ACTION_PAUSE);
    }

    /**
     * 继续上传
     * @param path
     * @param position
     */
    public void continueUpload(String path, int position){
        if(context == null)
            return;
        action2Service(path, position, FileUploadService.ACTION_CONTINUE);
    }

    private void action2Service(String path, int position, String action) {
        Intent intent = new Intent(context, FileUploadService.class);
        intent.setAction(action);
        intent.putExtra(PARAM_FILE_PATH, path);
        intent.putExtra(PARAM_POSITION, position);
        context.startService(intent);
    }

    private void action2Upload(FileUploadFileParam uploadFileParam, int position, String action) {
        Logger.i("upload Param: " + uploadFileParam);
        Intent intent = new Intent(context, FileUploadService.class);
        intent.setAction(action);
        intent.putExtra(PARAM_ALLOW_DOWN_COUNT, uploadFileParam.getAllowDownCount());
        intent.putExtra(PARAM_EXPIRE_TIME, uploadFileParam.getExpireTime());
        intent.putExtra(PARAM_FILE_PATH, uploadFileParam.getFilePath());
        intent.putExtra(PARAM_IS_PRIVATE, uploadFileParam.isPrivate());
        intent.putExtra(PARAM_IS_SHARE, uploadFileParam.isShare());
        intent.putExtra(PARAM_DESCRIPTION, uploadFileParam.getDescription());
        intent.putExtra(PARAM_PARENT_ID, uploadFileParam.getParentId());
        intent.putExtra(PARAM_POSITION, position);
        intent.putExtra(PARAM_SCORE, uploadFileParam.getScore());
        context.startService(intent);
    }


}
