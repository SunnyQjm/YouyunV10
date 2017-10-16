package com.sunny.youyun.activity.person_file_manager.person_file_manager_index;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.YouyunResultDeal;
import com.sunny.youyun.activity.person_file_manager.item.FileItem;
import com.sunny.youyun.activity.person_file_manager.item.PathItem;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.EasyYouyunAPIManager;
import com.sunny.youyun.model.YouyunExceptionDeal;
import com.sunny.youyun.model.callback.SimpleListener;
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
 * Created by Sunny on 2017/9/13 0013.
 */

class PersonFileManagerModel implements PersonFileManagerContract.Model {
    private final PersonFileManagerPresenter mPresenter;
    private final List<MultiItemEntity> mList = new ArrayList<>();
    private final List<PathItem> pathItems = new ArrayList<>();

    PersonFileManagerModel(PersonFileManagerPresenter personFileManagerPresenter) {
        mPresenter = personFileManagerPresenter;
    }

    @Override
    public List<MultiItemEntity> getData() {
        return mList;
    }

    @Override
    public void getFilesByPath(String parentId, int page, int size, boolean isRefresh) {
        Observable<BaseResponseBody<FileItem[]>> observable = parentId != null ?
                APIManager.getInstance()
                        .getFileServices(GsonConverterFactory.create())
                        .getUploadFiles(parentId, page, size) :
                APIManager.getInstance()
                        .getFileServices(GsonConverterFactory.create())
                        .getUploadFiles(page, size);
        observable
                .map(baseResponseBody -> {
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
                        YouyunResultDeal.deal(integer, new YouyunResultDeal.OnResultListener() {
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
                        mPresenter.dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        YouyunExceptionDeal.getInstance()
                                .deal(mPresenter.getView(), e);
                        mPresenter.dismissDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void delete(final String id, final int position) {
        EasyYouyunAPIManager.delete(id, new SimpleListener() {
            @Override
            public void onSuccess() {
                mPresenter.deleteSuccess(position);
            }

            @Override
            public void onFail() {

            }

            @Override
            public void onError(Throwable e) {
                Logger.e("删除文件夹或文件失败", e);
            }

            @Override
            public void onSubscribe(Disposable d) {
                mPresenter.addSubscription(d);
            }
        });
    }

    @Override
    public List<PathItem> getPaths() {
        return pathItems;
    }
}
