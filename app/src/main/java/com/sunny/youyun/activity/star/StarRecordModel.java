package com.sunny.youyun.activity.star;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.model.YouyunResultDeal;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.data_item.StarRecord;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

class StarRecordModel implements StarRecordContract.Model{
    private final StarRecordPresenter mPresenter;
    private final List<StarRecord> mList = new ArrayList<>();
    StarRecordModel(StarRecordPresenter starRecordPresenter) {
        mPresenter = starRecordPresenter;
    }

    @Override
    public void getStarRecord(int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .getStarRecord(page, size)
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
                                mPresenter.getStarRecordSuccess();
                            }

                            @Override
                            public void onLoadFinish() {
                                mPresenter.allDataLoadFinish();
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("获取点赞列表失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public List<StarRecord> getDatas() {
        return mList;
    }
}
