package com.sunny.youyun.activity.concern;

/**
 * Created by Sunny on 2017/9/11 0011.
 */

class ConcernModel implements ConcernContract.Model {
    private final ConcernPresenter mPresenter;

    ConcernModel(ConcernPresenter concernPresenter) {
        mPresenter = concernPresenter;
    }
}
