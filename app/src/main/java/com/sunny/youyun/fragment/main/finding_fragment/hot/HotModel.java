package com.sunny.youyun.fragment.main.finding_fragment.hot;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.YouyunResultDeal;
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
 * Created by Sunny on 2017/9/14 0014.
 */

class HotModel implements HotContract.Model{
    private final HotPresenter mPresenter;
    private final List<InternetFile> mList = new ArrayList<>();
    HotModel(HotPresenter hotPresenter) {
        mPresenter = hotPresenter;
    }

    @Override
    public List<InternetFile> getDatas() {
        return mList;
    }

    @Override
    public void getForumDataHot(int page, boolean isRefresh) {
        APIManager.getInstance()
                .getForumServices(GsonConverterFactory.create())
                .getForumAll(page, ApiInfo.GET_DEFAULT_SIZE, false, true)
                .map(baseResponseBody -> {
                    if(baseResponseBody.isSuccess() &&
                            baseResponseBody.getData() != null){
                        if(isRefresh)
                            mList.clear();
                        Collections.addAll(mList, baseResponseBody.getData());
                        if(baseResponseBody.getData().length < ApiInfo.GET_DEFAULT_SIZE){
                            return ApiInfo.RESULT_DEAL_TYPE_LOAD_FINISH;
                        }
                        return ApiInfo.RESULT_DEAL_TYPE_SUCCESS;
                    } else {
                        return ApiInfo.RESULT_DEAL_TYPE_FAIL;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        YouyunResultDeal.deal(integer, new YouyunResultDeal.OnResultListener() {
                            @Override
                            public void onSuccess() {
                                mPresenter.getDataSuccess();
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
                        YouyunExceptionDeal.getInstance()
                                .deal(mPresenter.getView(), e);
                        Logger.e("获取社区信息失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
