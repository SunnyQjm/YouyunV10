package com.sunny.youyun.activity.login;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.QQLoginResult;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.model.YouyunExceptionDeal;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.model.response_body.LoginResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/6/6 0006.
 */

class LoginModel implements LoginContract.Model{

    private LoginPresenter mPresenter;
    LoginModel(LoginPresenter loginPresenter) {
        mPresenter = loginPresenter;
    }

    @Override
    public void login(String username, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiInfo.LOGIN_USERNAME, username);
            jsonObject.put(ApiInfo.LOGIN_PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .login(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponseBody>() {
                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "登录失败");
                        YouyunExceptionDeal.getInstance()
                                .deal(mPresenter.getView(), e);
                        mPresenter.showError("登录失败，请检查网络连接");
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(LoginResponseBody loginResponseBody) {
                        if(loginResponseBody.isSuccess()){      //判断是否登录成功
                            UserInfoManager.getInstance().setUserInfo(loginResponseBody.getData());
                            YouyunAPI.updateIsLogin(true);
                            YouyunAPI.updateLoginMode(YouyunAPI.LOGIN_MODE_NORMAL);
                            YouyunAPI.updateLoginToken(loginResponseBody.getData().getLoginToken());
                            mPresenter.loginSuccess();
                        }else{
                            YouyunAPI.updateIsLogin(false);
                            mPresenter.showTip("登录失败，请检查用户名或密码是否正确");
                        }
                    }
                });
    }

    @Override
    public void qqLogin(QQLoginResult result) {
        if(result.nameValuePairs == null){
            mPresenter.showError("登陆失败");
            Logger.i("login fail");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiInfo.QQ_LOGIN_ACCESS_TOKEN, result.nameValuePairs.access_token);
            jsonObject.put(ApiInfo.QQ_LOGIN_OPEN_ID, result.nameValuePairs.openid);
            jsonObject.put(ApiInfo.LOGIN_TYPE, ApiInfo.LOGIN_TY_QQ);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());

        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .qqLogin(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponseBody>() {
                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e, "登录失败");
                        mPresenter.showTip("登录失败，请检查网络连接");
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(LoginResponseBody loginResponseBody) {
                        if(loginResponseBody.isSuccess() && loginResponseBody.getData() != null){      //判断是否登录成功
                            YouyunAPI.updateIsLogin(true);
                            YouyunAPI.updateLoginMode(YouyunAPI.LOGIN_MODE_QQ);
                            UserInfoManager.getInstance().setUserInfo(loginResponseBody.getData());
                            YouyunAPI.updateLoginToken(loginResponseBody.getData().getLoginToken());
                            mPresenter.loginSuccess();
                        }else{
                            YouyunAPI.updateIsLogin(false);
                            mPresenter.showTip(loginResponseBody.getMsg());
                        }
                    }
                });
    }
}
