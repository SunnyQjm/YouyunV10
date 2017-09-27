package com.sunny.youyun.activity.person_file_manager.person_file_manager_index;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.internet.exception.LoginTokenInvalidException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

class PersonFileManagerModel implements PersonFileManagerContract.Model {
    private final PersonFileManagerPresenter mPresenter;
    private final List<MultiItemEntity> mList = new ArrayList<>();

    PersonFileManagerModel(PersonFileManagerPresenter personFileManagerPresenter) {
        mPresenter = personFileManagerPresenter;
    }

    @Override
    public List<MultiItemEntity> getData() {
        return mList;
    }

    @Override
    public void getUploadFilesOnline(String parent) {
        APIManager.getInstance()
                .getFileServices(GsonConverterFactory.create())
                .getUploadFiles()
                .map(baseResponseBody -> {
                    if (baseResponseBody.isSuccess()) {
                        mList.clear();
                        Collections.addAll(mList, baseResponseBody.getData());
                        return true;
                    }
                    return false;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mPresenter.getUploadFilesSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("获取上传文件列表失败: " + e.getMessage(), e);
                        if(e.getClass() == LoginTokenInvalidException.class){
                            mPresenter.showError("登录失效，请重新登录");
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void createDirectory(String parentId, String name) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiInfo.CREATE_DIRECTORY_NAME, name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(
                MediaType.parse(ApiInfo.MEDIA_TYPE_JSON), jsonObject.toString()
        );
        APIManager.getInstance()
                .getFileServices(GsonConverterFactory.create())
                .createDirectory(body)
                .map(baseResponseBody -> {
                    if (baseResponseBody.isSuccess()) {
                        mList.clear();
                        Collections.addAll(mList, baseResponseBody.getData());
                        return true;
                    }
                    return false;
                })
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mPresenter.createDirectorySuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("创建文件夹失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
