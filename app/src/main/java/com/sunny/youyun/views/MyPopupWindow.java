package com.sunny.youyun.views;

import android.content.Context;
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
    private int width = ViewGroup.LayoutParams.MATCH_PARENT;
    private int height = ViewGroup.LayoutParams.WRAP_CONTENT;

    public MyPopupWindow(Context context, int width, int height){
        super(context);
        this.width = width;
        this.height = height;
        init();
    }
    public MyPopupWindow(View contentView, int width, int height) {
        this(contentView, width, height, true);
    }

    private MyPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        this.width = width;
        this.height = height;
        init();
    }

    private void init() {
        this.setWidth(width);
        this.setHeight(height);
        this.setAnimationStyle(R.style.BottomPopupWindowStyle);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setFocusable(true);
        this.setOutsideTouchable(false);
    }

}
