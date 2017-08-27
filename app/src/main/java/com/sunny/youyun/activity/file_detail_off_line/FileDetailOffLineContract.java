package com.sunny.youyun.activity.file_detail_off_line;

import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/8/19 0019.
 */

interface FileDetailOffLineContract {
    interface View extends BaseView {
        void showDetail(InternetFile internetFile);
    }

    interface Model extends BaseModel {
        void getFileInfo(String code);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void getFileInfo(String code);

        abstract void showDetail(InternetFile internetFile);
    }
}
