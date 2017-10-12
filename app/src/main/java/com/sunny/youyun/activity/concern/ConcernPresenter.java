package com.sunny.youyun.activity.concern;

import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.data_item.ConcernItem;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/9/11 0011.
 */

class ConcernPresenter extends ConcernContract.Presenter{
    ConcernPresenter(ConcernActivity concernActivity) {
        mView = concernActivity;
        mModel = new ConcernModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    List<ConcernItem> getData() {
        return mModel.getData();
    }

    @Override
    void getFollowingList(int page, boolean isRefresh) {
        mModel.getFollowingList(page, ApiInfo.GET_DEFAULT_SIZE, isRefresh);
    }

    @Override
    void getFollowingSuccess() {
        mView.getFollowingSuccess();
    }

    @Override
    public void allDataLoadFinish() {
        mView.allDataLoadFinish();
    }
}
