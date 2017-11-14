package com.sunny.youyun.activity.person_info.other_file_fragment;

import android.os.Handler;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.internet.rx.RxObserver;
import com.sunny.youyun.internet.rx.RxResultHelper;
import com.sunny.youyun.internet.rx.RxSchedulersHelper;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.YouyunExceptionDeal;
import com.sunny.youyun.model.YouyunResultDeal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/10/10 0010.
 */

class OtherPublicShareFileModel implements OtherPublicShareFileContract.Model {
    private final OtherPublicShareFilePresenter mPresenter;
    private final List<InternetFile> mList = new ArrayList<>();
    OtherPublicShareFileModel(OtherPublicShareFilePresenter otherPublicShareFilePresenter) {
        mPresenter = otherPublicShareFilePresenter;
    }

    @Override
    public List<InternetFile> getData() {
        return mList;
    }

    @Override
    public void getFiles(int userId, int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .getOtherPublicFiles(userId, page, size)
                .compose(RxResultHelper.INSTANCE.handlePageResult(mList, isRefresh))
                .compose(RxSchedulersHelper.INSTANCE.io_main())
                .subscribe(new RxObserver<Integer>(mPresenter) {
                    @Override
                    public void _onNext(Integer integer) {
                        YouyunResultDeal.INSTANCE.deal(integer, new YouyunResultDeal.OnResultListener() {
                            @Override
                            public void onSuccess() {
                                mPresenter.getOtherPublicFilesSuccess();
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
