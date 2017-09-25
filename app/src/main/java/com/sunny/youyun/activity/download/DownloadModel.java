package com.sunny.youyun.activity.download;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/8/9 0009.
 */

class DownloadModel implements DownloadContract.Model {
    private DownloadPresenter mPresenter;

    DownloadModel(DownloadPresenter downloadPresenter) {
        this.mPresenter = downloadPresenter;
    }

    @Override
    public void getFileInfo(String code) {
        APIManager.getInstance()
                .getFileServices(GsonConverterFactory.create())
                .getFileInfo(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody<InternetFile>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseResponseBody<InternetFile> baseResponseBody) {
                        if(baseResponseBody.isSuccess() && baseResponseBody.getData() != null){
                            mPresenter.showDetail(baseResponseBody.getData());
                        } else {
                            mPresenter.showTip("文件不存在，请输入正确的提取码");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPresenter.showError("获取文件信息失败");
                        Logger.e("获取文件信息失败", e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("completed");
                    }
                });
    }
}
