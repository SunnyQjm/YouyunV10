package com.sunny.youyun.fragment.main.main_fragment;

import android.os.Handler;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.App;
import com.sunny.youyun.internet.upload.FileUploadFileParam;
import com.sunny.youyun.internet.upload.FileUploader;
import com.sunny.youyun.model.InternetFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/6/22 0022.
 */

class MainFragmentModel implements MainFragmentContract.Model {
    private MainFragmentPresenter mPresenter;
    private final List<InternetFile> mUploadList = App.mList_UploadRecord;
    private final Handler handler = new Handler();

    MainFragmentModel(MainFragmentPresenter mainFragmentPresenter) {
        mPresenter = mainFragmentPresenter;
    }


    /**
     * 文件上传操作
     */
    @Override
    public void uploadFile(FileUploadFileParam uploadFileParam) throws IOException {
        if (uploadFileParam.getFilePath() == null)
            return;

        final File f = new File(uploadFileParam.getFilePath());
        if (!f.exists()) {
            mPresenter.showError("文件不存在");
            Logger.e("文件不存在");
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
        synchronized (mUploadList) {
            position = mUploadList.size();
            mUploadList.add(internetFile);
        }

        FileUploader.getInstance()
                .upload(uploadFileParam, position);
    }
}
