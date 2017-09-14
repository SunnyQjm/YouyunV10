package com.sunny.youyun.fragment.main.finding_fragment.concern;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

class ConcernModel implements ConcernContract.Model{
    private final ConcernPresenter mPresenter;
    ConcernModel(ConcernPresenter concernPresenter) {
        mPresenter = concernPresenter;
    }
}
