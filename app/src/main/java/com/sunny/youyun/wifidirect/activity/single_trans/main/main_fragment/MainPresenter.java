package com.sunny.youyun.wifidirect.activity.single_trans.main.main_fragment;

/**
 * Created by Sunny on 2017/8/10 0010.
 */

class MainPresenter extends MainContract.Presenter{
    MainPresenter(MainFragment mainFragment) {
        mView = mainFragment;
        mModel = new MainModel(this);
    }

    @Override
    protected void start() {

    }
}
