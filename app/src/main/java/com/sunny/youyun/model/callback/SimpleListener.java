package com.sunny.youyun.model.callback;

import io.reactivex.disposables.Disposable;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

public interface SimpleListener {
    void onSuccess();
    void onFail();
    void onError(Throwable e);
    void onSubscribe(Disposable d);
}
