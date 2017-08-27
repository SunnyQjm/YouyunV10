package com.sunny.youyun.activity.download;

import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/8/9 0009.
 */

public interface DownloadContract {
    interface View extends BaseView {
        void showDetail(InternetFile data);
    }

    interface Model extends BaseModel {
        void getFileInfo(String code);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void getFileInfo(String code);

        abstract void showDetail(InternetFile data);
    }
}
