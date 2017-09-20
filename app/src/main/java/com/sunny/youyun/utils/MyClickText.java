package com.sunny.youyun.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.sunny.youyun.R;

/**
 * Created by Sunny on 2017/9/17 0017.
 */

public class MyClickText extends ClickableSpan {
    private Context context;
    private OnTextClickListener mListener;

    public MyClickText(@NonNull final Context context, OnTextClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(context.getResources().getColor(R.color.application_theme_color));
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        if(mListener != null)
            mListener.onClick(widget);
    }

    public interface OnTextClickListener{
        void onClick(View target);
    }
}
