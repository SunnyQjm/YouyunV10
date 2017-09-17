package com.sunny.youyun.activity.setting;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/8/30 0030.
 */

interface SettingContract {
    interface View extends BaseView {

    }

    interface Model extends BaseModel {
        void logout();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void logout();
    }
}
