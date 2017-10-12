package com.sunny.youyun.fragment.main.message_fragment;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.YouyunResultDeal;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.message_fragment.item.PrivateLetterItem;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.YouyunExceptionDeal;
import com.sunny.youyun.model.manager.MessageManager;

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

class MessageModel implements MessageContract.Model {
    private final MessagePresenter mPresenter;
    private final List<MultiItemEntity> mList = new ArrayList<>();

    MessageModel(MessagePresenter messagePresenter) {
        mPresenter = messagePresenter;
    }

    @Override
    public List<MultiItemEntity> getData() {
        return mList;
    }

    @Override
    public void getPrivateLetterList(int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getChatServices(GsonConverterFactory.create())
                .getPrivateLetterList(page, size)
                .map(baseResponseBody -> {
                    if (baseResponseBody.isSuccess() &&
                            baseResponseBody.getData() != null) {
                        if (isRefresh)
                            remove();
                        addAll(baseResponseBody.getData());
                        return ApiInfo.RESULT_DEAL_TYPE_SUCCESS;
                    }
                    return ApiInfo.RESULT_DEAL_TYPE_FAIL;
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
                                mPresenter.getPrivateLetterListSuccess();
                            }

                            @Override
                            public void onLoadFinish() {

                            }

                            @Override
                            public void onFail() {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("获取私信列表失败", e);
                        YouyunExceptionDeal.getInstance()
                                .deal(mPresenter.getView(), e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void addAll(PrivateLetterItem[] privateLetters) {
        for (PrivateLetterItem l : privateLetters) {
            mList.add(l);
            MessageManager.getInstance()
                    .put(l.getId(), l);
        }
    }

    private void remove() {
        MessageManager.getInstance()
                .clearMessage();
        while (mList.size() > 2) {
            mList.remove(2);
        }
    }
}
