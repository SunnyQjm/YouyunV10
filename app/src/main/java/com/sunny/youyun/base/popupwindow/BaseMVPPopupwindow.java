package com.sunny.youyun.base.popupwindow;

import android.support.v7.app.AppCompatActivity;

import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;
import com.sunny.youyun.views.EasyDialog;
import com.sunny.youyun.views.MyPopupWindow;
import com.sunny.youyun.views.youyun_dialog.loading.YouyunLoadingView;
import com.sunny.youyun.views.youyun_dialog.qr.YouyunQRDialog;
import com.sunny.youyun.views.youyun_dialog.tip.YouyunTipDialog;

/**
 * Created by Sunny on 2017/10/14 0014.
 */

public abstract class BaseMVPPopupwindow<P extends BasePresenter> extends MyPopupWindow
        implements BaseView {
    protected P mPresenter;
    protected AppCompatActivity activity;

    protected YouyunTipDialog dialog;
    protected YouyunLoadingView loadingView;


    public BaseMVPPopupwindow(AppCompatActivity activity, int width, int height) {
        super(activity, width, height);
        if (mPresenter == null) {
            mPresenter = onCreatePresenter();
        }
        this.activity = activity;
    }

    protected abstract P onCreatePresenter();

    /**
     * 显示提示
     *
     * @param info 要提示的信息
     */
    public void showTip(String info) {
        dismissDialog();
        if (dialog == null)
            dialog = EasyDialog.showTip(activity, info);
        else
            dialog.show(activity.getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    public void showSuccess(String info) {
        dismissDialog();
        if (dialog == null)
            dialog = EasyDialog.showSuccess(activity, info);
        else
            dialog.show(activity.getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    public void showError(String info) {
        dismissDialog();
        if (dialog == null)
            dialog = EasyDialog.showError(activity, info);
        else
            dialog.show(activity.getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    public YouyunQRDialog showQrDialog(String content) {
        YouyunQRDialog qrDialog = YouyunQRDialog.newInstance(activity, content);
        qrDialog.show(activity.getSupportFragmentManager(), String.valueOf(this.getClass()));
        return qrDialog;
    }

    public void showLoading() {
        dismissDialog();
        loadingView = EasyDialog.showLoading(activity);
    }

    @Override
    public void dismissDialog() {
        if (dialog != null && !dialog.isHidden())
            dialog.dismiss();
        if (loadingView != null && loadingView.isShowing())
            loadingView.dismiss();
    }
}
