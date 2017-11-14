package com.sunny.youyun.views.popupwindow.directory_select;

import com.sunny.youyun.model.YouyunResultDeal;
import com.sunny.youyun.activity.person_file_manager.item.FileItem;
import com.sunny.youyun.activity.person_file_manager.item.PathItem;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.YouyunExceptionDeal;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/10/14 0014.
 */

class DirectSelectModel implements DirectSelectContract.Model {
    private final DirectSelectPresenter mPresenter;
    private final List<MultiItemEntity> mList = new ArrayList<>();
    private final List<PathItem> pathItems = new ArrayList<>();

    DirectSelectModel(DirectSelectPresenter directSelectPresenter) {
        mPresenter = directSelectPresenter;
    }

    @Override
    public List<MultiItemEntity> getDatas() {
        return mList;
    }

    @Override
    public void getFilesByPath(String parentId, int page, int size, boolean isRefresh) {
        Observable<BaseResponseBody<FileItem[]>> observable =
                parentId == null ? APIManager.getInstance()
                        .getFileServices(GsonConverterFactory.create())
                        .getUploadFiles() :
                        APIManager.getInstance()
                                .getFileServices(GsonConverterFactory.create())
                                .getUploadFiles(parentId, true);
        observable.map(baseResponseBody -> {
            if (baseResponseBody.isSuccess() && baseResponseBody.getData() != null) {
                if (isRefresh)
                    mList.clear();
                Collections.addAll(mList, baseResponseBody.getData());
                if (baseResponseBody.getData().length < ApiInfo.GET_DEFAULT_SIZE)
                    return ApiInfo.RESULT_DEAL_TYPE_LOAD_FINISH;
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
                        YouyunResultDeal.INSTANCE.deal(integer, new YouyunResultDeal.OnResultListener() {
                            @Override
                            public void onSuccess() {
                                mPresenter.getFilesByPathSuccess();
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
                        YouyunExceptionDeal.getInstance()
                                .deal(mPresenter.getView(), e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public List<PathItem> getPaths() {
        return pathItems;
    }
}
