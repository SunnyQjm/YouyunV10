package com.sunny.youyun.internet.upload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;


import com.orhanobut.logger.Logger;
import com.sunny.youyun.App;
import com.sunny.youyun.model.InternetFile;

import java.io.File;

import static com.sunny.youyun.internet.upload.FileUploadService.*;

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
