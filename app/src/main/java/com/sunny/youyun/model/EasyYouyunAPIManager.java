package com.sunny.youyun.model;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.activity.login.LoginActivity;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.model.response_body.BaseResponseBody;
import com.sunny.youyun.utils.JPushUtil;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.share.TencentUtil;

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
        logout_(activity);
        RouterUtils.open(activity, IntentRouter.LoginActivity);
    }

    public static void logout(Fragment fragment){
        logout_(fragment.getActivity());
        fragment.startActivity(new Intent(fragment.getContext(), LoginActivity.class));
    }

    private static void logout_(Activity activity){
        //清空用户数据
        UserInfoManager.getInstance().clear();
        YouyunAPI.updateIsLogin(false);
        JPushUtil.setTag(activity, "0000");
        //QQ登出
        if(YouyunAPI.getLoginMode() == YouyunAPI.LOGIN_MODE_QQ){
            TencentUtil.getInstance(activity)
                    .loginOut();
        }
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
