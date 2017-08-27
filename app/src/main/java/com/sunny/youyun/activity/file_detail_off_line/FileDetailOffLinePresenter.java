package com.sunny.youyun.activity.file_detail_off_line;

import com.sunny.youyun.model.InternetFile;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/19 0019.
 */

class FileDetailOffLinePresenter extends FileDetailOffLineContract.Presenter{
    FileDetailOffLinePresenter(FileDetailOffLineActivity fileDetailOffLineActivity) {
        mView = fileDetailOffLineActivity;
        mModel = new FileDetailOffLineModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    void getFileInfo(String code) {
        mModel.getFileInfo(code);
    }

    @Override
    void showDetail(InternetFile internetFile) {
        mView.showDetail(internetFile);
    }
}
