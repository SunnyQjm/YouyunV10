package com.sunny.youyun.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.sunny.youyun.R;

/**
 * Created by Sunny on 2017/8/24 0024.
 */

public class MyPopupWindow extends PopupWindow {
    public MyPopupWindow(View contentView, int width, int height) {
        this(contentView, width, height, true);
    }

    public MyPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        init();
    }

    private void init() {
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setAnimationStyle(R.style.PopupWindowStyle);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setFocusable(true);
        this.setOutsideTouchable(false);
    }
}
