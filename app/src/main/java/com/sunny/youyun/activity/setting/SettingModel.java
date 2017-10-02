package com.sunny.youyun.activity.setting;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
                .subscribe(new Observer<BaseResponseBody<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<String> stringBaseResponseBody) {
                        Logger.i("Login: " + stringBaseResponseBody.isSuccess());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("退出登录失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
