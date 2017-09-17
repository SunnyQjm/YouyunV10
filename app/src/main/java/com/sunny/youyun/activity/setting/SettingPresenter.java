package com.sunny.youyun.activity.setting;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/30 0030.
 */

class SettingPresenter extends SettingContract.Presenter{
    SettingPresenter(SettingActivity settingActivity) {
        mView = settingActivity;
        mModel = new SettingModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    void logout() {
        mModel.logout();
    }
}
