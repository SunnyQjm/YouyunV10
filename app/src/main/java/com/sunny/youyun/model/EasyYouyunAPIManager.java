package com.sunny.youyun.model;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.activity.login.LoginActivity;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.callback.SimpleListener;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.model.response_body.BaseResponseBody;
import com.sunny.youyun.utils.JPushUtil;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.share.TencentUtil;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/10/9 0009.
 */

public class EasyYouyunAPIManager {

    ///////////////////////////////////////////////////////
    /////////createNewDirectory
    //////////////////////////////////////////////////////
    public static void createNewDirectory(String parentId, String name, SimpleListener listener){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiInfo.CREATE_DIRECTORY_NAME, name);
            if(parentId != null){
                jsonObject.put(ApiInfo.CREATE_DIRECTORY_PARENT_ID, parentId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(
                MediaType.parse(ApiInfo.MEDIA_TYPE_JSON), jsonObject.toString()
        );
        APIManager.getInstance()
                .getFileServices(GsonConverterFactory.create())
                .createDirectory(body)
                .map(baseResponseBody -> baseResponseBody != null && baseResponseBody.isSuccess())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(aBoolean){
                            if(listener != null)
                                listener.onSuccess();
                        } else if(listener != null){
                            listener.onFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if(listener != null)
                            listener.onError(e);
                        Logger.e("创建文件夹失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    ///////////////////////////////////////////////////////
    /////////logout
    //////////////////////////////////////////////////////
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
