package com.sunny.youyun.activity.login;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.model.model_interface.UserInterface;
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

//        APIManager.getInstance()
//                .getUserService(GsonConverterFactory.create())
//                .login(username, password)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<User>() {
//                    @Override
//                    public void onCompleted() {
//                        Logger.i("completed");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Logger.e(e, "login failed");
//                    }
//
//                    @Override
//                    public void onNext(User user) {
//                        Logger.i(user.toString());
//                    }
//                });
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", username);
            jsonObject.put("password", password);
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
                        if(loginResponseBody.isSuccess()){      //判断是否登录成功
                            UserInterface.setIsLogin(true);
                            UserInterface.setUserInfo(loginResponseBody.getData());
                            YouyunAPI.updateIsLogin(true);
                            mPresenter.loginSuccess();
                        }else{
                            YouyunAPI.updateIsLogin(false);
                            mPresenter.showTip(loginResponseBody.getMsg());
                        }
                    }
                });
    }
}
