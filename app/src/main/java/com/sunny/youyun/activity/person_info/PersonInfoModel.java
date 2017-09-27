package com.sunny.youyun.activity.person_info;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.response_body.BaseResponseBody;
import com.sunny.youyun.model.result.GetUserInfoResult;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/8/30 0030.
 */

class PersonInfoModel implements PersonInfoContract.Model {
    private PersonInfoPresenter mPresenter;

    PersonInfoModel(PersonInfoPresenter settingPresenter) {
        mPresenter = settingPresenter;
    }

    @Override
    public void getUserInfoOnLine() {
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .getUserIno()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<GetUserInfoResult>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<GetUserInfoResult> getUserInfoResultBaseResponseBody) {
                        if(getUserInfoResultBaseResponseBody.isSuccess()){
                            mPresenter.getUserInfoSuccess(getUserInfoResultBaseResponseBody.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Logger.e("获取用户信息失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
