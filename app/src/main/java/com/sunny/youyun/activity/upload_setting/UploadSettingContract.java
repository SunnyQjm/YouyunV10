package com.sunny.youyun.activity.upload_setting;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/8/16 0016.
 */

interface UploadSettingContract {
    interface View extends BaseView {
        void updateUI();
    }

    interface Model extends BaseModel {
        List<MultiItemEntity> getData(String[] paths);
        void addData(String[] paths);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract List<MultiItemEntity> getData(String[] paths);

        abstract void updateUI();

        abstract void addData(String[] paths);
    }

}
