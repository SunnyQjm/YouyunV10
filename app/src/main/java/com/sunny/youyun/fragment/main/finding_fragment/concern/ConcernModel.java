package com.sunny.youyun.fragment.main.finding_fragment.concern;

import com.sunny.youyun.internet.rx.RxObserver;
import com.sunny.youyun.internet.rx.RxResultHelper;
import com.sunny.youyun.internet.rx.RxSchedulersHelper;
import com.sunny.youyun.model.YouyunResultDeal;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.InternetFile;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
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
                .compose(RxResultHelper.INSTANCE.handlePageResult(mList, isRefresh))
                .compose(RxSchedulersHelper.INSTANCE.io_main())
                .subscribe(new RxObserver<Integer>(mPresenter) {
                    @Override
                    public void _onNext(Integer integer) {
                        YouyunResultDeal.INSTANCE.deal(integer, new YouyunResultDeal.OnResultListener() {
                            @Override
                            public void onSuccess() {
                                mPresenter.getDatasOnlineSuccess();
                            }

                            @Override
                            public void onLoadFinish() {
                                mPresenter.allDataLoadFinish();
                            }

                            @Override
                            public void onFail() {
                                System.out.println("fail");
                            }
                        });
                    }
                });
    }
}
