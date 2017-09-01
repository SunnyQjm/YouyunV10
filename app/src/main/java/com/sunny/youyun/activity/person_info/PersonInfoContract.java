package com.sunny.youyun.activity.person_info;

import com.sunny.youyun.model.result.GetUserInfoResult;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/8/30 0030.
 */

interface PersonInfoContract {
    interface View extends BaseView {
        void getUserInfoSuccess(GetUserInfoResult result);
    }

    interface Model extends BaseModel {
        void getUserInfoOnLine();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void getUserInfoOnline();
        abstract void getUserInfoSuccess(GetUserInfoResult result);
    }
}
