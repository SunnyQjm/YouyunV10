package com.sunny.youyun.activity.person_info.concern_fragment;

import com.sunny.youyun.mvp.BaseView;

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
}
