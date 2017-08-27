package com.sunny.youyun.activity.file_manager.fragment.other;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

interface OtherContract {
    interface View extends BaseView {
        void updateUI();
    }

    interface Model extends BaseModel {
        void back();

        boolean isRootPath();

        void show(int position);

        List<MultiItemEntity> getData();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void updateUI();
        abstract List<MultiItemEntity> getData();
        abstract boolean isRootPath();

        abstract void back();
        abstract void show(int position);

    }
}
