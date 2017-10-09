package com.sunny.youyun.model;

import android.app.Activity;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.model.response_body.BaseResponseBody;
import com.sunny.youyun.utils.JPushUtil;
import com.sunny.youyun.utils.RouterUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/10/9 0009.
 */

public class EasyYouyunAPIManager {
    public static void logout(Activity activity){
        //清空用户数据
        UserInfoManager.getInstance().clear();
        logout();
        YouyunAPI.updateIsLogin(false);
        JPushUtil.setTag(activity, "0000");
        RouterUtils.open(activity, IntentRouter.LoginActivity);
    }

    private static void logout(){
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
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
