package com.sunny.youyun.activity.my_collection;

import com.sunny.youyun.model.data_item.Collection;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

interface MyCollectionContract {
    interface View extends BaseView {
        void getCollectionsSuccess();
        void allDataLoadFinish();
    }

    interface Model extends BaseModel {
        List<Collection> getData();
        void getCollections(int page, int size, boolean isRefresh);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void getCollections(int page, boolean isRefresh);
        abstract List<Collection> getData();
        abstract void getCollectionsSuccess();
        abstract void allDataLoadFinish();
    }
}
