package com.sunny.youyun.activity.comment;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.YouyunResultDeal;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.data_item.CommentRecord;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

class CommentRecordModel implements CommentRecordContract.Model {
    private final CommentRecordPresenter mPresenter;
    private final List<CommentRecord> mList = new ArrayList<>();
    CommentRecordModel(CommentRecordPresenter commentRecordPresenter) {
        mPresenter = commentRecordPresenter;
    }

    @Override
    public List<CommentRecord> getDatas() {
        return mList;
    }

    @Override
    public void getCommentRecord(int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .getCommentRecord(page, size)
                .map(baseResponseBody -> YouyunResultDeal.dealData(baseResponseBody, mList, isRefresh))
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
                                mPresenter.getCommentRecordSuccess();
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
                        Logger.e("获取评论记录失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
