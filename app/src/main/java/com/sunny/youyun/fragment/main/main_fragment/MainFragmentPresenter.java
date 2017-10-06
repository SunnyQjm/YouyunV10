package com.sunny.youyun.fragment.main.main_fragment;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.upload.FileUploadFileParam;
import com.sunny.youyun.mvp.BaseView;

import java.io.IOException;

/**
 * Created by Sunny on 2017/6/22 0022.
 */

class MainFragmentPresenter extends MainFragmentContract.Presenter {

    public MainFragmentPresenter(BaseView baseView) {
        mView = (MainFragmentContract.View) baseView;
        mModel = new MainFragmentModel(this);
    }

    @Override
    protected void start() {

    }

    @Override
    void uploadFile(FileUploadFileParam uploadFileParam) {
        System.out.println("UPLOAD PARAM: " + uploadFileParam);
        try {
            mModel.uploadFile(uploadFileParam);
        } catch (IOException e) {
            Logger.e(e, "上传文件错误");
            mView.showError("上传错误");
        }
    }

    @Override
    void uploadSuccess(String info) {
        mView.uploadSuccess(info);
    }
}
