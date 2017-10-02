package com.sunny.youyun.activity.concern;

import com.sunny.youyun.model.data_item.ConcernItem;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/9/11 0011.
 */

interface ConcernContract {
    interface View extends BaseView {
        void getFollowingSuccess();
        void allDataGetFinish();
    }

    interface Model extends BaseModel {
        List<ConcernItem> getData();
        void getFollowingList(int page, int size, boolean isRefresh);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract List<ConcernItem> getData();
        abstract void getFollowingList(int page, boolean isRefresh);
        abstract void getFollowingSuccess();
        abstract void allDataGetFinish();
    }
}
