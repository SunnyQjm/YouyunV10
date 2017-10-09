package com.sunny.youyun.model;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.exception.LoginTokenInvalidException;
import com.sunny.youyun.views.EasyDialog;
import com.sunny.youyun.views.youyun_dialog.tip.YouyunTipDialog;

/**
 * Created by Sunny on 2017/10/9 0009.
 */

public enum  YouyunExceptionDeal {
    INSTANCE;
    public static YouyunExceptionDeal getInstance(){
        return INSTANCE;
    }

    private YouyunTipDialog dialog = null;

    public void deal(Context context, Throwable e){
        if(context == null || !(context instanceof AppCompatActivity)){
            Logger.e("错误处理出错：context not instanceof AppCompatActivity" );
        }
        deal((AppCompatActivity)context, e);
    }
    public void deal(AppCompatActivity appCompatActivity, Throwable e){
        //登陆失效，需要重新登录
        if(e instanceof LoginTokenInvalidException){
            showReLogin(appCompatActivity);
        }
    }

    private void showReLogin(AppCompatActivity appCompatActivity){
        dismissDialog();
        dialog = EasyDialog.showReLogin(appCompatActivity);
    }

    public void dismissDialog() {
        if (dialog != null && !dialog.isHidden())
            dialog.dismiss();
    }
}
