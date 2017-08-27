package com.sunny.youyun.activity.register;

import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/22 0022.
 */

class RegisterPresenter extends RegisterContract.Presenter{

    public RegisterPresenter(BaseView view) {
        mView = (RegisterContract.View) view;
        mModel = new RegisterModel(this);
    }

    @Override
    protected void start() {

    }

    @Override
    void sendCode(String phone) {
        mModel.sendCode(phone);
    }

    @Override
    void register(String phone, String nickname, String password, String code) {
        mModel.register(phone, nickname, password, code);
    }

    @Override
    void registerSuccess(String info) {
        mView.registerSuccess(info);
    }
}
