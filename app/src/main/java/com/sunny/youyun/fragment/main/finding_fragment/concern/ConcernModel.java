package com.sunny.youyun.fragment.main.finding_fragment.concern;

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
 * Created by Sunny on 2017/9/14 0014.
 */

class ConcernModel implements ConcernContract.Model{
    private final ConcernPresenter mPresenter;
    private final List<InternetFile> mList = new ArrayList<>();
    ConcernModel(ConcernPresenter concernPresenter) {
        mPresenter = concernPresenter;
    }

    @Override
    public List<InternetFile> getDatas() {
        return mList;
    }

    @Override
    public void getConcernPeopleShares(int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getForumServices(GsonConverterFactory.create())
                .getConcernPeopleShares(page, size)
                .map(internetFileBaseResponseBody -> {
                    if(internetFileBaseResponseBody.isSuccess() &&
                            internetFileBaseResponseBody.getData() != null){
                        if(isRefresh)
                            mList.clear();
                        Collections.addAll(mList, internetFileBaseResponseBody.getData());
                        if(internetFileBaseResponseBody.getData().length < ApiInfo.GET_DEFAULT_SIZE){
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
                        switch (integer){
                            case ApiInfo.RESULT_DEAL_TYPE_FAIL:
                                break;
                            case ApiInfo.RESULT_DEAL_TYPE_LOAD_FINISH:
                                mPresenter.allDataLoadFinish();
                            case ApiInfo.RESULT_DEAL_TYPE_SUCCESS:
                                mPresenter.getDatasOnlineSuccess();
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("加载关注的人动态失败", e);
                        YouyunExceptionDeal.getInstance()
                                .deal(mPresenter.getView(), e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
