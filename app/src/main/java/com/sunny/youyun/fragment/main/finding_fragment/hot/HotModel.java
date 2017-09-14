package com.sunny.youyun.fragment.main.finding_fragment.hot;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

class HotModel implements HotContract.Model{
    private final HotPresenter mPresenter;
    HotModel(HotPresenter hotPresenter) {
        mPresenter = hotPresenter;
    }
}
