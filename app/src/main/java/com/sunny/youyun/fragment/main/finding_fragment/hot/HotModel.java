package com.sunny.youyun.fragment.main.finding_fragment.hot;

import com.sunny.youyun.internet.rx.RxObserver;
import com.sunny.youyun.internet.rx.RxResultHelper;
import com.sunny.youyun.internet.rx.RxSchedulersHelper;
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

class HotModel implements HotContract.Model {
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
                .compose(RxResultHelper.INSTANCE.handlePageResult(mList, isRefresh))
                .compose(RxSchedulersHelper.INSTANCE.io_main())
                .subscribe(new RxObserver<Integer>(mPresenter) {
                    @Override
                    public void _onNext(Integer integer) {
                        YouyunResultDeal.INSTANCE.deal(integer, new YouyunResultDeal.OnResultListener() {
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
                });
    }
}
