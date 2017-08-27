package com.sunny.youyun.activity.file_manager.fragment.picture;

import android.content.Context;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

interface PictureContract {
    interface View extends BaseView {
        void updateUI();
    }

    interface Model extends BaseModel {
        List<MultiItemEntity> getData();
        void refreshData(Context context);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void updateUI();


        abstract List<MultiItemEntity> getData();
        abstract void refreshData(Context context);
    }
}
