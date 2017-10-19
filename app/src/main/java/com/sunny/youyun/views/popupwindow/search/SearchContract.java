package com.sunny.youyun.views.popupwindow.search;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/10/19 0019.
 */

interface SearchContract {
    interface View extends BaseView {
        void searchSuccess();
    }

    interface Model extends BaseModel {
        void search(String str);
        List<MultiItemEntity> getData();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void search(String str);
        abstract void searchSuccess();
        abstract List<MultiItemEntity> getData();
    }
}
