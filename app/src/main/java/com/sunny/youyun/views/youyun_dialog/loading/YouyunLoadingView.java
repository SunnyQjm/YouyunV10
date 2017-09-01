package com.sunny.youyun.views.youyun_dialog.loading;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.sunny.youyun.R;


/**
 * Created by Sunny on 2017/8/28 0028.
 */

public class YouyunLoadingView extends ProgressDialog{
    public YouyunLoadingView(Context context) {
        this(context, R.style.YouyunLoadingView);
    }

    public YouyunLoadingView(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.loading_view_layout);
        Window window = getWindow();
        if(window == null)
            return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
    }
}
