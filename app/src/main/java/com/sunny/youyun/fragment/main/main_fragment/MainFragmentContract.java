package com.sunny.youyun.fragment.main.main_fragment;

import com.sunny.youyun.internet.upload.FileUploadFileParam;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.io.IOException;

/**
 * Created by Sunny on 2017/6/22 0022.
 */

interface MainFragmentContract {
    interface View extends BaseView {
        void uploadSuccess(String info);
    }

    interface Model extends BaseModel {
        void uploadFile(FileUploadFileParam uploadFileParam) throws IOException;

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void uploadFile(FileUploadFileParam uploadFileParam);

        abstract void uploadSuccess(String info);

    }

}
