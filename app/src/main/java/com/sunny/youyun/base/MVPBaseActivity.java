package com.sunny.youyun.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.sunny.youyun.App;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;
import com.sunny.youyun.views.EasyDialog;
import com.sunny.youyun.views.youyun_dialog.loading.YouyunLoadingView;
import com.sunny.youyun.views.youyun_dialog.tip.YouyunTipDialog;


/**
 * Created by Administrator on 2017/3/12 0012.
 */

public abstract class MVPBaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {

    protected P mPresenter;
    protected YouyunTipDialog dialog;
    protected YouyunLoadingView loadingView;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
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

    /**
     * 显示提示
     *
     * @param info 要提示的信息
     */
    public void showTip(String info) {
        dismissDialog();
        if (dialog == null)
            dialog = EasyDialog.showTip(this, info);
        else
            dialog.show(getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    public void showSuccess(String info) {
        dismissDialog();
        if (dialog == null)
            dialog = EasyDialog.showSuccess(this, info);
        else
            dialog.show(getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    public void showError(String info) {
        dismissDialog();
        if (dialog == null)
            dialog = EasyDialog.showError(this, info);
        else
            dialog.show(getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    protected void showLoading() {
        dismissDialog();
        loadingView = EasyDialog.showLoading(this);
    }

    protected void dismissDialog() {
        if (dialog != null && !dialog.isHidden())
            dialog.dismiss();
        if (loadingView != null && loadingView.isShowing())
            loadingView.dismiss();
    }

    @Override
    @CallSuper
    public void finish() {
        super.finish();
        dialog = null;
        loadingView = null;
        //5.0以下用老版本的切换动画
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            App.finishAnim(this);
        }
    }

    protected abstract P onCreatePresenter();
}
