package com.sunny.youyun.activity.person_info;

import com.sunny.youyun.model.User;
import com.sunny.youyun.model.result.GetUserInfoResult;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/30 0030.
 */

class PersonInfoPresenter extends PersonInfoContract.Presenter{

    PersonInfoPresenter(PersonInfoActivity settingActivity) {
        mView = settingActivity;
        mModel = new PersonInfoModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    void getUserInfoOnline() {
        mModel.getUserInfoOnLine();
    }

    @Override
    void getUserInfoSuccess(GetUserInfoResult result) {
        mView.getUserInfoSuccess(result);
    }

    @Override
    void concern(int otherId) {
        mModel.concern(otherId);
    }

    @Override
    void concernSuccess() {
        mView.concernSuccess();
    }

    @Override
    void getOtherUserInfoOnline(int otherId) {
        mModel.getOtherUserInfoOnline(otherId);
    }

    @Override
    void getOtherUserInfoSuccess(User user) {
        mView.getOtherUserInfoSuccess(user);
    }
}
