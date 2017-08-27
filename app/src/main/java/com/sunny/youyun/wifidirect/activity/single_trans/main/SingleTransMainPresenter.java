package com.sunny.youyun.wifidirect.activity.single_trans.main;

/**
 * Created by Sunny on 2017/8/9 0009.
 */

class SingleTransMainPresenter extends SingleTranMainContract.Presenter{
    SingleTransMainPresenter(SingleTransMainActivity singleTransMainActivity) {
        mView = singleTransMainActivity;
        mModel = new SingleTransMainModel(this);
    }

    @Override
    protected void start() {

    }
}
