package com.sunny.youyun.activity.person_info.dynamic_fragment;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.YouyunResultDeal;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.data_item.Dynamic;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

class DynamicModel implements DynamicContract.Model {
    private final DynamicPresenter mPresenter;
    private final List<Dynamic> mList = new ArrayList<>();

    DynamicModel(DynamicPresenter uploadRecordPresenter) {
        mPresenter = uploadRecordPresenter;
    }


    @Override
    public void getDynamic(int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .getUserDynamic(page, size)
                .map(baseResponseBody -> YouyunResultDeal.INSTANCE.dealData(baseResponseBody, mList, isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        YouyunResultDeal.INSTANCE.deal(integer, new YouyunResultDeal.OnResultListener() {
                            @Override
                            public void onSuccess() {
                                mPresenter.getDynamicSuccess();
                            }

                            @Override
                            public void onLoadFinish() {
                                mPresenter.allDataGetFinish();
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("获取动态失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getDynamic(int userId, int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .getUserDynamic(userId, page, size, "23")
                .map(baseResponseBody -> YouyunResultDeal.INSTANCE.dealData(baseResponseBody, mList, isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        YouyunResultDeal.INSTANCE.deal(integer, new YouyunResultDeal.OnResultListener() {
                            @Override
                            public void onSuccess() {
                                mPresenter.getDynamicSuccess();
                            }

                            @Override
                            public void onLoadFinish() {
                                mPresenter.allDataGetFinish();
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("获取动态失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public List<Dynamic> getData() {
        return mList;
    }
}
