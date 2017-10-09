package com.sunny.youyun.activity.person_info;

import com.sunny.youyun.model.User;
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

        void concernSuccess();

        void getOtherUserInfoSuccess(User user);
    }

    interface Model extends BaseModel {
        void getUserInfoOnLine();

        void getOtherUserInfoOnline(int otherId);

        void concern(int otherId);
    }

    abstract class Presenter extends BasePresenter<View, Model> {

        public Presenter(PersonInfoActivity settingActivity) {
            super(settingActivity);
        }

        abstract void getUserInfoOnline();

        abstract void getUserInfoSuccess(GetUserInfoResult result);

        abstract void concern(int otherId);

        abstract void concernSuccess();

        abstract void getOtherUserInfoOnline(int otherId);

        abstract void getOtherUserInfoSuccess(User user);
    }
}
