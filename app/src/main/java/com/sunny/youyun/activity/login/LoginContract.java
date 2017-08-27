package com.sunny.youyun.activity.login;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/6 0006.
 */

interface LoginContract {
    interface View extends BaseView {
        void loginSuccess();
    }

    interface Model extends BaseModel {
        void login(String username, String password);
    }

    abstract class Presenter extends BasePresenter<View, Model> {

        abstract void login(String username, String password);
        abstract void loginSuccess();
    }
}
