package com.sunny.youyun.base.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.sunny.youyun.App;
import com.sunny.youyun.views.EasyDialog;
import com.sunny.youyun.views.youyun_dialog.loading.YouyunLoadingView;
import com.sunny.youyun.views.youyun_dialog.qr.YouyunQRDialog;
import com.sunny.youyun.views.youyun_dialog.tip.YouyunTipDialog;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

public class YouyunActivity extends AppCompatActivity {

    protected YouyunTipDialog dialog = null;
    protected YouyunLoadingView loadingView = null;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    @CallSuper      //表示
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate presenter");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
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
        dialog = EasyDialog.showSuccess(this, info);
    }

    public void showError(String info) {
        dismissDialog();
        if (dialog == null)
            dialog = EasyDialog.showError(this, info);
        else
            dialog.show(getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    public void showLoading() {
        dismissDialog();
        loadingView = EasyDialog.showLoading(this);
    }

    public YouyunQRDialog showQrDialog(String content) {
        YouyunQRDialog youyunQRDialog = YouyunQRDialog.newInstance(this, content);
        youyunQRDialog.show(getSupportFragmentManager(), String.valueOf(this.getClass()));
        return youyunQRDialog;
    }

    public void dismissDialog() {
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
        App.finishAnim(this);
//        //5.0以下用老版本的切换动画
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            App.finishAnim(this);
//        }
    }
}
