package com.sunny.youyun.activity.person_info.dynamic_fragment;

import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.data_item.Dynamic;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class DynamicPresenter extends DynamicContract.Presenter{
    public DynamicPresenter(BaseView baseView) {
        mView = (DynamicContract.View) baseView;
        mModel = new DynamicModel(this);
    }

    @Override
    protected void start() {

    }

    @Override
    void getDynamic(int page, boolean isRefresh) {
        mModel.getDynamic(page, 10, isRefresh);
    }

    @Override
    void getDynamic(int userId, int page, boolean isRefresh) {
        mModel.getDynamic(userId, page, ApiInfo.GET_DEFAULT_SIZE, isRefresh);
    }

    @Override
    void getDynamicSuccess() {
        mView.getDynamicSuccess();
    }

    @Override
    List<Dynamic> getData() {
        return mModel.getData();
    }

    @Override
    void allDataGetFinish() {
        mView.allDataGetFinish();
    }
}
