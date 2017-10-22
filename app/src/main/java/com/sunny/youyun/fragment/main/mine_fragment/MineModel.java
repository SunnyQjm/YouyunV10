package com.sunny.youyun.fragment.main.mine_fragment;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

class MineModel implements MineContract.Model{
    private MinePresenter mPresenter;

    MineModel(MinePresenter minePresenter) {
        mPresenter = minePresenter;
    }
}
