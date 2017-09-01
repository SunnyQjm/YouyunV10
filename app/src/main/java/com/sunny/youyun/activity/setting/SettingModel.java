package com.sunny.youyun.activity.setting;

/**
 * Created by Sunny on 2017/8/30 0030.
 */

class SettingModel implements SettingContract.Model {
    private SettingPresenter mPresenter;
    SettingModel(SettingPresenter settingPresenter) {
        mPresenter = settingPresenter;
    }
}
