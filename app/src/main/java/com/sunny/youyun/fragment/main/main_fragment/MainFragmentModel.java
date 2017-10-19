package com.sunny.youyun.fragment.main.main_fragment;

/**
 * Created by Sunny on 2017/6/22 0022.
 */

class MainFragmentModel implements MainFragmentContract.Model {
    private MainFragmentPresenter mPresenter;

    MainFragmentModel(MainFragmentPresenter mainFragmentPresenter) {
        mPresenter = mainFragmentPresenter;
    }

}
