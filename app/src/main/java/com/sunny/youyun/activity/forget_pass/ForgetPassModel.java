package com.sunny.youyun.activity.forget_pass;

/**
 * Created by Sunny on 2017/8/26 0026.
 */

class ForgetPassModel implements ForgetPassContract.Model{
    private ForgetPassPresenter mPresenter;
    ForgetPassModel(ForgetPassPresenter forgetPassPresenter) {
        mPresenter = forgetPassPresenter;
    }
}
