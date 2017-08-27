package com.sunny.youyun.fragment.main.mine_fragment;

import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class MinePresenter extends MineContract.Presenter{


    public MinePresenter(BaseView baseView) {
        mView = (MineContract.View) baseView;
        mModel = new MineModel(this);
    }

    @Override
    protected void start() {

    }
}
