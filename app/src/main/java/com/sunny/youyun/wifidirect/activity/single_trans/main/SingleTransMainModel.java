package com.sunny.youyun.wifidirect.activity.single_trans.main;

/**
 * Created by Sunny on 2017/8/9 0009.
 */

class SingleTransMainModel implements SingleTranMainContract.Model{
    private SingleTransMainPresenter mPresenter;
    SingleTransMainModel(SingleTransMainPresenter singleTransMainPresenter) {
        mPresenter = singleTransMainPresenter;
    }
}
