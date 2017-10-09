package com.sunny.youyun.fragment.main.finding_fragment.hot;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.YouyunExceptionDeal;
import com.sunny.youyun.model.response_body.BaseResponseBody;
import com.sunny.youyun.utils.GsonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

class HotModel implements HotContract.Model{
    private final HotPresenter mPresenter;
    private final List<InternetFile> internetFiles = new ArrayList<>();
    HotModel(HotPresenter hotPresenter) {
        mPresenter = hotPresenter;
    }

    @Override
    public List<InternetFile> getDatas() {
        return internetFiles;
    }

    @Override
    public void getForumDataHot(int page, boolean isRefresh) {
        APIManager.getInstance()
                .getForumServices(GsonConverterFactory.create())
                .getForumAll(page, ApiInfo.GET_DEFAULT_SIZE, false, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<InternetFile[]>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<InternetFile[]> listBaseResponseBody) {
                        Logger.i(GsonUtil.getInstance().toJson(listBaseResponseBody));
                        if(listBaseResponseBody.isSuccess()){
                            InternetFile[] datas = listBaseResponseBody.getData();
                            //如果是更新操作则先清空数据
                            if(isRefresh){
                                internetFiles.clear();
                            }
                            Collections.addAll(internetFiles, datas);
                            if(datas.length < ApiInfo.GET_DEFAULT_SIZE){
                                mPresenter.allDataLoadFinish();
                            }
                            mPresenter.getDataSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        YouyunExceptionDeal.getInstance()
                                .deal(mPresenter.getView(), e);
                        Logger.e("获取社区信息失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
