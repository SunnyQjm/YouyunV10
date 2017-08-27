package com.sunny.youyun.activity.main;

/**
 * Created by Sunny on 2017/6/24 0024.
 */

public class MainModel implements MainContract.Model{
    private MainPresenter mPresenter;
    public MainModel(MainPresenter mainPresenter) {
        mPresenter = mainPresenter;
    }
}
