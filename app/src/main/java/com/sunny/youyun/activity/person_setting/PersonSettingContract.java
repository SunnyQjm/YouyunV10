package com.sunny.youyun.activity.person_setting;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/10/2 0002.
 */
interface PersonSettingContract {
    interface View extends BaseView {
        void modifyUserInfoSuccess();
    }

    interface Model extends BaseModel {
        void modifyUserInfo(String username, int sex, String signature, String oldPassword,
                            String newPassword);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void modifyUserName(String username);
        abstract void modifySignature(String signature);
        abstract void modifySex(int sex);
        abstract void modifyPassword(String oldPassword, String newPassword);
        abstract void modifyUserInfoSuccess();
    }
}
