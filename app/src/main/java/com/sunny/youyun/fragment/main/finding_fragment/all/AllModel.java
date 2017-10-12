package com.sunny.youyun.fragment.main.finding_fragment.all;

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

class AllModel implements AllContract.Model{
    private final AllPresenter mPresenter;
    private final List<InternetFile> mList = new ArrayList<>();
    AllModel(AllPresenter allPresenter) {
        mPresenter = allPresenter;
    }

    @Override
    public void getForumDataALL(int page, boolean isRefresh) {
        APIManager.getInstance()
                .getForumServices(GsonConverterFactory.create())
                .getForumAll(page, ApiInfo.GET_DEFAULT_SIZE, true, false)
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
                                mPresenter.getForumDataSuccess();
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

//                    @Override
//                    public void onNext(BaseResponseBody<InternetFile[]> listBaseResponseBody) {
//                        Logger.i(GsonUtil.getInstance().toJson(listBaseResponseBody));
//                        if(listBaseResponseBody.isSuccess()){
//                            InternetFile[] datas = listBaseResponseBody.getData();
//                            //如果是更新操作则先清空数据
//                            if(isRefresh){
//                                mList.clear();
//                            }
//                            Collections.addAll(mList, datas);
//                            if(datas.length < ApiInfo.GET_DEFAULT_SIZE){
//                                mPresenter.allDataLoadFinish();
//                            }
//                            mPresenter.getForumDataSuccess();
//                        }
//                    }

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

    @Override
    public List<InternetFile> getDatas() {
        return mList;
    }
}
