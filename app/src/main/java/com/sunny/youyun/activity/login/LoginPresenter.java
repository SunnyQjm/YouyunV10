package com.sunny.youyun.activity.login;

import com.sunny.youyun.model.QQLoginResult;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/6 0006.
 */

class LoginPresenter extends LoginContract.Presenter{
    @Override
    protected void start() {

    }
    LoginPresenter(BaseView view){
        this.mView = (LoginContract.View) view;
        this.mModel = new LoginModel(this);
    }

    @Override
    void login(String username, String password) {
        mModel.login(username, password);
    }

    @Override
    void qqLogin(QQLoginResult result) {
        mModel.qqLogin(result);
    }

    @Override
    void loginSuccess() {
        mView.loginSuccess();
    }
}
