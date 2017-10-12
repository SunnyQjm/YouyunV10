package com.sunny.youyun.activity.person_info.other_file_fragment;

import android.os.Handler;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.YouyunExceptionDeal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/10/10 0010.
 */

class OtherPublicShareFileModel implements OtherPublicShareFileContract.Model {
    private final OtherPublicShareFilePresenter mPresenter;
    private final List<InternetFile> mList = new ArrayList<>();
    private final Handler handler = new Handler();
    OtherPublicShareFileModel(OtherPublicShareFilePresenter otherPublicShareFilePresenter) {
        mPresenter = otherPublicShareFilePresenter;
    }

    @Override
    public List<InternetFile> getData() {
        return mList;
    }

    @Override
    public void getFiles(int userId, int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .getOtherPublicFiles(userId, page, size)
                .map(internetFileBaseResponseBody -> {
                    Logger.i("获取他人文件Map，thread：" + Thread.currentThread().getName());
                    if(internetFileBaseResponseBody.isSuccess() &&
                            internetFileBaseResponseBody.getData() != null){
                        if(isRefresh)
                            mList.clear();
                        Collections.addAll(mList, internetFileBaseResponseBody.getData());
                        if (internetFileBaseResponseBody.getData().length < ApiInfo.GET_DEFAULT_SIZE)
                            handler.post(mPresenter::allDataGetFinish);
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
                        if(aBoolean)
                            mPresenter.getOtherPublicFilesSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("获取他人公开文件列表", e);
                        YouyunExceptionDeal.getInstance()
                                .deal(mPresenter.getView(), e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
