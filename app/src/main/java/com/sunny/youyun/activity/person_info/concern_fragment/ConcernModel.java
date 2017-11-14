package com.sunny.youyun.activity.person_info.concern_fragment;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.rx.RxObserver;
import com.sunny.youyun.internet.rx.RxResultHelper;
import com.sunny.youyun.internet.rx.RxSchedulersHelper;
import com.sunny.youyun.model.YouyunResultDeal;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.data_item.ConcernItem;

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

class ConcernModel implements ConcernContract.Model {
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
                .compose(RxResultHelper.INSTANCE.handlePageResult(mList, isRefresh))
                .compose(RxSchedulersHelper.INSTANCE.io_main())
                .subscribe(new RxObserver<Integer>(mPresenter) {
                    @Override
                    public void _onNext(Integer integer) {
                        YouyunResultDeal.INSTANCE.deal(integer, new YouyunResultDeal.OnResultListener() {
                            @Override
                            public void onSuccess() {
                                mPresenter.getFollowingListSuccess();
                            }

                            @Override
                            public void onLoadFinish() {
                                mPresenter.allDataGetFinish();
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                    }
                });
    }

    @Override
    public void getFollowingList(int userId, int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .getFollowingList(userId, page, size)
                .compose(RxResultHelper.INSTANCE.handlePageResult(mList, isRefresh))
                .compose(RxSchedulersHelper.INSTANCE.io_main())
                .subscribe(new RxObserver<Integer>(mPresenter) {
                    @Override
                    public void _onNext(Integer integer) {
                        YouyunResultDeal.INSTANCE.deal(integer, new YouyunResultDeal.OnResultListener() {
                            @Override
                            public void onSuccess() {
                                mPresenter.getFollowingListSuccess();
                            }

                            @Override
                            public void onLoadFinish() {
                                mPresenter.allDataGetFinish();
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                    }
                });
    }

}
