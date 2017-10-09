package com.sunny.youyun.activity.register;

import android.util.Log;

import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.YouyunExceptionDeal;
import com.sunny.youyun.model.response_body.BaseResponseBody;
import com.sunny.youyun.model.response_body.RegisterResponseBody;
import com.sunny.youyun.utils.GsonUtil;

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
 * Created by Sunny on 2017/6/22 0022.
 */

class RegisterModel implements RegisterContract.Model{
    private RegisterPresenter mPresenter;
    RegisterModel(RegisterPresenter registerPresenter) {
        mPresenter = registerPresenter;
    }

    @Override
    public void sendCode(String phone) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .sendCode(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<String>>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.e(RegisterModel.class.getName(), "send code fail!!");
                        YouyunExceptionDeal.getInstance()
                                .deal(mPresenter.getView(), e);
                        mPresenter.showError("发送验证码失败，请检查网络连接");
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        //添加到订阅清单，当Activity被销毁时取消订阅
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<String> stringBaseResponseBody) {
                        if(stringBaseResponseBody.isSuccess()){
                            mPresenter.showSuccess("发送验证码成功");
                        }else {
                            mPresenter.showTip(stringBaseResponseBody.getMsg());
                        }
                    }
                });
    }

    @Override
    public void register(String phone, String nickname, String password, String code) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiInfo.REGISTER_PHONE, phone);
            jsonObject.put(ApiInfo.REGISTER_NICKNAME, nickname);
            jsonObject.put(ApiInfo.REGISTER_PASSWORD, password);
            jsonObject.put(ApiInfo.REGISTER_CODE, code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse(ApiInfo.MEDIA_TYPE_JSON), jsonObject.toString());

        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .register(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegisterResponseBody>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.e(RegisterModel.class.getName(), "register fail!!");
                        mPresenter.showError("注册失败，请检查网络连接");
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(RegisterResponseBody registerResponseBody) {
                        if(registerResponseBody.isSuccess()){
                            mPresenter.registerSuccess(GsonUtil.getInstance().toJson(registerResponseBody));
                        }else {
                            mPresenter.showTip(registerResponseBody.getMsg());
                        }
                    }
                });
    }
}
