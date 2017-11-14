package com.sunny.youyun.activity.concern;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.model.YouyunResultDeal;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.YouyunExceptionDeal;
import com.sunny.youyun.model.data_item.ConcernItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/9/11 0011.
 */

class ConcernModel implements ConcernContract.Model {
    private final ConcernPresenter mPresenter;
    private List<ConcernItem> mList = new ArrayList<>();

    ConcernModel(ConcernPresenter concernPresenter) {
        mPresenter = concernPresenter;
    }

    @Override
    public List<ConcernItem> getData() {
        return mList;
    }

    @Override
    public void getFollowingList(int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .getFollowingList(page, size)
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
                                mPresenter.getFollowingSuccess();
                            }

                            @Override
                            public void onLoadFinish() {
                                mPresenter.allDataLoadFinish();
                            }

                            @Override
                            public void onFail() {
                                Logger.e("获取关注的人列表失败");
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        YouyunExceptionDeal.getInstance()
                                .deal(mPresenter.getView(), e);
                        Logger.e("获取关注的人列表失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
