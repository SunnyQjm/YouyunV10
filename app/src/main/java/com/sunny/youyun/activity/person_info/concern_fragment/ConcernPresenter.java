package com.sunny.youyun.activity.person_info.concern_fragment;

import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.data_item.ConcernItem;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class ConcernPresenter extends ConcernContract.Presenter{
    public ConcernPresenter(BaseView b) {
        super();
        mView = (ConcernContract.View) b;
        mModel = new ConcernModel(this);
    }

    @Override
    protected void start() {

    }

    @Override
    void beginListen() {
        mModel.beginListen();
    }

    @Override
    List<ConcernItem> getData() {
        return mModel.getData();
    }

    @Override
    void getFollowingList(int page, boolean isRefresh) {
        mModel.getFollowingList(page, ApiInfo.GET_FORUM_DEFAULT_SIZE, isRefresh);
    }

    @Override
    void getFollowingListSuccess() {
        mView.getFollowingListSuccess();
    }
}
