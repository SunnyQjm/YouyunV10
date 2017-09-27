package com.sunny.youyun.activity.person_file_manager.person_file_list;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.activity.person_file_manager.item.BaseFileItem;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.APIManager;
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
 * Created by Sunny on 2017/9/25 0025.
 */

class PersonFileListModel implements PersonFileListContract.Model{
    private final PersonFileListPresenter mPresenter;
    private final List<MultiItemEntity> mList = new ArrayList<>();
    PersonFileListModel(PersonFileListPresenter personFileListPresenter) {
        mPresenter = personFileListPresenter;
    }

    @Override
    public List<MultiItemEntity> getData() {
        return mList;
    }

    @Override
    public void getFileByType(String MIME, int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getUserService(GsonConverterFactory.create())
                .getUserFileByType(MIME, page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<BaseFileItem[]>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<BaseFileItem[]> baseResponseBody) {
                        if(baseResponseBody.isSuccess()){
                            if(isRefresh){
                                mList.clear();
                            }
                            Collections.addAll(mList, baseResponseBody.getData());
                            mPresenter.getFileByTypeSuccess();
                        } else {
                            Logger.e("根据类型获取文件失败: " + baseResponseBody.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("根据类型获取文件失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getFileByPath(String parentId) {

    }
}
