package com.sunny.youyun.base.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;


/**
 * Created by Administrator on 2017/3/12 0012.
 */

public abstract class MVPBaseActivity<P extends BasePresenter> extends YouyunActivity implements BaseView {

    protected P mPresenter;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    @CallSuper      //表示
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate presenter");
        mPresenter = onCreatePresenter();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        //当Activity被销毁时，取消所有订阅
        if (mPresenter != null) {
            mPresenter.clearAllDisposable();
        }
    }


    protected abstract P onCreatePresenter();


    @Override
    public void dismissDialog() {
        super.dismissDialog();
    }
}
