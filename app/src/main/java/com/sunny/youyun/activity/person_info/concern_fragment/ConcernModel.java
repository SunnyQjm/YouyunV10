package com.sunny.youyun.activity.person_info.concern_fragment;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.data_item.ConcernItem;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

class ConcernModel implements ConcernContract.Model{
    private final ConcernPresenter mPresenter;
    private final List<ConcernItem> mList = new ArrayList<>();
    ConcernModel(ConcernPresenter downloadRecordPresenter) {
        mPresenter = downloadRecordPresenter;
    }

    @Override
    public void beginListen() {

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<ConcernItem[]>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<ConcernItem[]> baseResponseBody) {
                        if(baseResponseBody.isSuccess() && baseResponseBody.getData() != null){
                            if(isRefresh){
                                mList.clear();
                            }
                            Collections.addAll(mList, baseResponseBody.getData());
                            mPresenter.getFollowingListSuccess();
                            if(baseResponseBody.getData().length < ApiInfo.GET_DEFAULT_SIZE)
                                mPresenter.allDataGetFinish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("获取关注的人信息失败", e);

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
