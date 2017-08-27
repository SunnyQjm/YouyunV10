package com.sunny.youyun.activity.forget_pass;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/26 0026.
 */

class ForgetPassPresenter extends ForgetPassContract.Presenter{
    ForgetPassPresenter(ForgetPassActivity forgetPassActivity) {
        this.mView = forgetPassActivity;
        this.mModel = new ForgetPassModel(this);
    }

    @Override
    protected void start() throws IOException {

    }
}
