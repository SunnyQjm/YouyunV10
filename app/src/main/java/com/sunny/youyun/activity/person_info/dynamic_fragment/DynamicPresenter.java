package com.sunny.youyun.activity.person_info.dynamic_fragment;

import com.sunny.youyun.mvp.BaseView;

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

}
