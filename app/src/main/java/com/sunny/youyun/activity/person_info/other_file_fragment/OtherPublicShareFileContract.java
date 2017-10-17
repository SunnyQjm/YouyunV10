package com.sunny.youyun.activity.person_info.other_file_fragment;

import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/10/10 0010.
 */

interface OtherPublicShareFileContract {
    interface View extends BaseView {

        void getOtherPublicFilesSuccess();

        void allDataLoadFinish();
    }

    interface Model extends BaseModel {
        List<InternetFile> getData();

        void getFiles(int userId, int page, int size, boolean b);
    }

    abstract class Presenter extends BasePresenter<View, Model> {

        abstract List<InternetFile> getData();

        abstract void getFiles(int userId, int page, boolean b);

        abstract void getOtherPublicFilesSuccess();

        abstract void allDataGetFinish();
    }
}
