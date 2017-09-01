package com.sunny.youyun.activity.clip;

import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

class ClipImageModel implements ClipImageContrat.Model {
    private ClipImagePresenter mPresenter;

    ClipImageModel(ClipImagePresenter clipImagePresenter) {
        mPresenter = clipImagePresenter;
    }

    @Override
    public void saveFile(Observable<File> saveFileTO) {
        saveFileTO
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(File file) {
                        if (file != null)
                            updateFileToServer(file);
                        else
                            mPresenter.showError("裁剪失败");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPresenter.showError("裁剪失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateFileToServer(File file) {

        MultipartBody.Part mpf = MultipartBody.Part.createFormData(ApiInfo.MODIFY_AVATAR_AVATAR,
                file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));

        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .modifyAvatar(mpf)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<String> stringBaseResponseBody) {
                        System.out.println(stringBaseResponseBody);
                        if(stringBaseResponseBody.isSuccess()){
                            //更新头像信息
                            UserInfoManager.getInstance()
                                    .updateAvatar(stringBaseResponseBody.getData());
                            mPresenter.updateSuccess();
                        } else {
                            mPresenter.showError("头像修改失败，请检查网络连接");
                            mPresenter.updateFail();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPresenter.showError("头像修改失败，请检查网络连接");
                        mPresenter.updateFail();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
