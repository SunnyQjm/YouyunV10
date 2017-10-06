package com.sunny.youyun.activity.my_collection;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.data_item.Collection;
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
 * Created by Sunny on 2017/9/13 0013.
 */

class MyCollectionModel implements MyCollectionContract.Model{
    private final MyCollectionPresenter mPresenter;
    private final List<Collection> mList = new ArrayList<>();
    MyCollectionModel(MyCollectionPresenter myCollectionPresenter) {
        mPresenter = myCollectionPresenter;
    }

    @Override
    public List<Collection> getData() {
        return mList;
    }

    @Override
    public void getCollections(int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .getUserCollections(page, size, ApiInfo.GET_USER_DYNAMIC_TYPE_COLLECT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<Collection[]>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<Collection[]> baseResponseBody) {
                        if(baseResponseBody.isSuccess() && baseResponseBody.getData() != null){
                            if(isRefresh){
                                mList.clear();
                            }
                            Collections.addAll(mList, baseResponseBody.getData());
                            mPresenter.getCollectionsSuccess();
                            if(baseResponseBody.getData().length< ApiInfo.GET_FORUM_DEFAULT_SIZE)
                                mPresenter.allDataLoadFinish();
                        } else {
                            mPresenter.showError("获取失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("获取用户收藏失败哦", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
