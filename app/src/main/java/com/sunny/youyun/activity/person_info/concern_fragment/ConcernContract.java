package com.sunny.youyun.activity.person_info.concern_fragment;

import com.sunny.youyun.model.data_item.ConcernItem;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public interface ConcernContract {
    interface View extends BaseView {
        void getFollowingListSuccess();
        void allDataGetFinish();
    }
    interface Model extends BaseModel {
        void beginListen();
        List<ConcernItem> getData();
        void getFollowingList(int page, int size, boolean isRefresh);
        void getFollowingList(int userId, int page, int size, boolean isRefresh);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void beginListen();
        abstract List<ConcernItem> getData();
        abstract void getFollowingList(int page, boolean isRefresh);
        abstract void getFollowingList(int userId, int page, boolean isRefresh);
        abstract void getFollowingListSuccess();
        abstract void allDataGetFinish();
    }
}
