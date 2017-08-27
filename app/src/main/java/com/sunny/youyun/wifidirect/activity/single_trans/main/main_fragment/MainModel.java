package com.sunny.youyun.wifidirect.activity.single_trans.main.main_fragment;

/**
 * Created by Sunny on 2017/8/10 0010.
 */

class MainModel implements MainContract.Model{
    private MainPresenter mPresenter;
    MainModel(MainPresenter mainPresenter) {
        mPresenter = mainPresenter;
    }
}
