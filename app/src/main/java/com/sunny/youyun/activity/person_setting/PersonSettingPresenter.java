package com.sunny.youyun.activity.person_setting;

import com.sunny.youyun.utils.idling.EspressoIdlingResource;

import java.io.IOException;

/**
 * Created by Sunny on 2017/10/2 0002.
 */

class PersonSettingPresenter extends PersonSettingContract.Presenter{
    PersonSettingPresenter(PersonSettingActivity personSettingActivity) {
        mView = personSettingActivity;
        mModel = new PersonSettingModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    void modifyUserName(String username) {
        mModel.modifyUserInfo(username, -1, null, null, null);
        EspressoIdlingResource.getInstance()
                .increment();
    }

    @Override
    void modifySignature(String signature) {
        mModel.modifyUserInfo(null, -1, signature, null, null);
        EspressoIdlingResource.getInstance()
                .increment();
    }

    @Override
    void modifySex(int sex) {
        mModel.modifyUserInfo(null, sex, null, null, null);
        EspressoIdlingResource.getInstance()
                .increment();
    }

    @Override
    void modifyPassword(String oldPassword, String newPassword) {
        mModel.modifyUserInfo(null, -1, null, oldPassword, newPassword);
        EspressoIdlingResource.getInstance()
                .increment();
    }

    @Override
    void modifyUserInfoSuccess() {
        mView.modifyUserInfoSuccess();

    }
}
