package com.sunny.youyun.views;

import android.support.v7.app.AppCompatActivity;

import com.sunny.youyun.R;
import com.sunny.youyun.views.youyun_dialog.tip.OnYouyunTipDialogClickListener;
import com.sunny.youyun.views.youyun_dialog.tip.YouyunTipDialog;

/**
 * Created by Sunny on 2017/8/26 0026.
 */

public class EasyDialog {
    public static YouyunTipDialog tipDialog = null;
    public static YouyunTipDialog errorDialog = null;
    public static YouyunTipDialog successDialog = null;

    public static YouyunTipDialog showTip(AppCompatActivity appCompatActivity, String info) {
        if (tipDialog == null) {
            tipDialog = YouyunTipDialog.newInstance(R.drawable.icon_tip, info,
                    new OnYouyunTipDialogClickListener() {
                        @Override
                        public void onCancelClick() {
                            tipDialog.dismiss();
                        }

                        @Override
                        public void onSureClick() {
                            tipDialog.dismiss();
                        }
                    });
            //do not show sure and cancel btn
            tipDialog.setBtnVisible(false);
        } else {
            tipDialog.setText(info);
        }
        tipDialog.show(appCompatActivity.getSupportFragmentManager(), "TIP_DIALOG_TAG");
        return tipDialog;
    }

    public static YouyunTipDialog showError(AppCompatActivity appCompatActivity, String errInfo) {
        if (errorDialog == null) {
            errorDialog = YouyunTipDialog.newInstance(R.drawable.icon_error, errInfo,
                    new OnYouyunTipDialogClickListener() {
                        @Override
                        public void onCancelClick() {
                            errorDialog.dismiss();
                        }

                        @Override
                        public void onSureClick() {
                            errorDialog.dismiss();
                        }
                    });
            errorDialog.setBtnVisible(false);
        } else {
            errorDialog.setText(errInfo);
        }
        errorDialog.show(appCompatActivity.getSupportFragmentManager(), "ERR_DIALOG_TAG");
        return errorDialog;
    }

    public static YouyunTipDialog showSuccess(AppCompatActivity appCompatActivity, String successInfo) {
        if (successDialog == null) {
            successDialog = YouyunTipDialog.newInstance(R.drawable.icon_success, successInfo,
                    new OnYouyunTipDialogClickListener() {
                        @Override
                        public void onCancelClick() {
                            successDialog.dismiss();
                        }

                        @Override
                        public void onSureClick() {
                            successDialog.dismiss();
                        }
                    });
            successDialog.setBtnVisible(false);
        } else {
            successDialog.setText(successInfo);
        }
        successDialog.show(appCompatActivity.getSupportFragmentManager(), "SUCCESS_DIALOG_TAG");
        return successDialog;
    }
}
