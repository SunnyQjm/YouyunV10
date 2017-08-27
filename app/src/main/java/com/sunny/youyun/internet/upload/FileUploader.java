package com.sunny.youyun.internet.upload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;


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
     * @param position
     */
    public void upload(FileUploadFileParam uploadFileParam, int position){
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
        Intent intent = new Intent(context, FileUploadService.class);
        intent.setAction(action);
        intent.putExtra(PARAM_ALLOW_DOWN_COUNT, uploadFileParam.getAllowDownCount());
        intent.putExtra(PARAM_EXPIRE_TIME, uploadFileParam.getExpireTime());
        intent.putExtra(PARAM_FILE_PATH, uploadFileParam.getFilePath());
        intent.putExtra(PARAM_IS_PRIVATE, uploadFileParam.isPrivate());
        intent.putExtra(PARAM_IS_SHARE, uploadFileParam.isShare());
        intent.putExtra(PARAM_PARENT_ID, uploadFileParam.getParentId());
        intent.putExtra(PARAM_POSITION, position);
        intent.putExtra(PARAM_SCORE, uploadFileParam.getScore());
        context.startService(intent);
    }


}
