package com.sunny.youyun.activity.person_setting;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.response_body.BaseResponseBody;

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
 * Created by Sunny on 2017/10/2 0002.
 */

class PersonSettingModel implements PersonSettingContract.Model {
    private final PersonSettingPresenter mPresenter;

    PersonSettingModel(PersonSettingPresenter personSettingPresenter) {
        mPresenter = personSettingPresenter;
    }

    @Override
    public void modifyUserInfo(String username, int sex, String signature, String oldPassword, String newPassword) {
        JSONObject jsonObject = new JSONObject();
        try {
            if(sex > 0)
                jsonObject.put(ApiInfo.MODIFY_USER_INFO_SEX, sex);
            if(username != null)
                jsonObject.put(ApiInfo.MODIFY_USER_INFO_NICKNAME, username);
            if(signature != null)
                jsonObject.put(ApiInfo.MODIFY_USER_INFO_SIGNATURE, signature);
            if(oldPassword != null)
                jsonObject.put(ApiInfo.MODIFY_USER_INFO_OLD_PASSWORD, oldPassword);
            if(newPassword != null)
                jsonObject.put(ApiInfo.MODIFY_USER_INFO_NEW_PASSWORD, newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse(ApiInfo.MEDIA_TYPE_JSON),
                jsonObject.toString());
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .modifyUserInfo(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody baseResponseBody) {
                        if(baseResponseBody.isSuccess()){
                            mPresenter.modifyUserInfoSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("修改用户信息失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
