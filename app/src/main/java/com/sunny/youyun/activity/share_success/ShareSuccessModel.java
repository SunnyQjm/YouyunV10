package com.sunny.youyun.activity.share_success;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/10/22 0022.
 */

class ShareSuccessModel implements ShareSuccessContract.Model{
    private ShareSuccessPresenter mPresenter;
    ShareSuccessModel(ShareSuccessPresenter shareSuccessPresenter) {
        mPresenter = shareSuccessPresenter;
    }


}
