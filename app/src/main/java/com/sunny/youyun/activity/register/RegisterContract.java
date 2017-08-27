package com.sunny.youyun.activity.register;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/22 0022.
 */

interface RegisterContract {
    interface View extends BaseView {
        void registerSuccess(String info);
    }

    interface Model extends BaseModel {
        void sendCode(String phone);
        void register(String phone, String nickname, String password, String code);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void sendCode(String phone);
        abstract void register(String phone, String nickname, String password, String code);
        abstract void registerSuccess(String info);
    }
}
