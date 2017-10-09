package com.sunny.youyun.fragment.main.finding_fragment;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.finding_fragment.item.FileItem;
import com.sunny.youyun.fragment.main.finding_fragment.item.FileTag;
import com.sunny.youyun.model.data_item.SearchData;
import com.sunny.youyun.fragment.main.finding_fragment.item.UserItem;
import com.sunny.youyun.fragment.main.finding_fragment.item.UserTag;
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
 * Created by Sunny on 2017/6/25 0025.
 */

class FindingModel implements FindingContract.Model {
    private final FindingPresenter mPresenter;
    private final List<MultiItemEntity> mList = new ArrayList<>();
    FindingModel(FindingPresenter findingPresenter) {
        mPresenter = findingPresenter;
    }

    @Override
    public void search(String str) {
        mPresenter.showLoading();
        APIManager.getInstance()
                .getForumServices(GsonConverterFactory.create())
                .search(str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<SearchData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<SearchData> searchDataBaseResponseBody) {
                        if(searchDataBaseResponseBody.isSuccess() &&
                                searchDataBaseResponseBody.getData() != null){
                            mList.clear();
                            UserItem[] users = searchDataBaseResponseBody.getData().getUsers();
                            FileItem[] files = searchDataBaseResponseBody.getData().getFiles();
                            if(users != null && users.length > 0){
                                mList.add(new UserTag());
                                Collections.addAll(mList, users);
                            }
                            if(files != null && files.length > 0){
                                mList.add(new FileTag());
                                Collections.addAll(mList, files);
                            }
                            mPresenter.searchSuccess();
                        }
                        mPresenter.dismissDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPresenter.dismissDialog();
                        Logger.e("搜索失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public List<MultiItemEntity> getData() {
        return mList;
    }
}
