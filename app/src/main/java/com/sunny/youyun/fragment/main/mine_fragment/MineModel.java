package com.sunny.youyun.fragment.main.mine_fragment;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class MineModel implements MineContract.Model{
    private MinePresenter mPresenter;
    public MineModel(MinePresenter minePresenter) {
        mPresenter = minePresenter;
    }
}
