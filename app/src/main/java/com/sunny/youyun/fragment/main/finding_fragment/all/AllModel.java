package com.sunny.youyun.fragment.main.finding_fragment.all;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.rx.RxObserver;
import com.sunny.youyun.internet.rx.RxSchedulersHelper;
import com.sunny.youyun.internet.rx.RxResultHelper;
import com.sunny.youyun.model.YouyunResultDeal;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.InternetFile;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
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
                .compose(RxResultHelper.INSTANCE.handlePageResult(mList, isRefresh))
                .compose(RxSchedulersHelper.INSTANCE.io_main())
                .subscribe(new RxObserver<Integer>() {
                    @Override
                    public void _onNext(Integer integer) {
                        YouyunResultDeal.INSTANCE.deal(integer, new YouyunResultDeal.OnResultListener() {
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

                    @Override
                    public void _onError(@NotNull String msg) {
//                        YouyunExceptionDeal.getInstance()
//                                .deal(mPresenter.getView(), e);
                        Logger.e(msg);
                    }

                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        mPresenter.addSubscription(d);
                    }
                });
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        mPresenter.addSubscription(d);
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    @Override
    public List<InternetFile> getDatas() {
        return mList;
    }
}
