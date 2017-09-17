package com.sunny.youyun.activity.setting;

import com.sunny.youyun.internet.api.APIManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/8/30 0030.
 */

class SettingModel implements SettingContract.Model {
    private SettingPresenter mPresenter;
    SettingModel(SettingPresenter settingPresenter) {
        mPresenter = settingPresenter;
    }

    @Override
    public void logout() {
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
