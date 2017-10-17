package com.sunny.youyun.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Sunny on 2017/8/26 0026.
 */

public class WindowUtil {
    public static void changeWindowAlpha(Activity activity, boolean open){
        if(open)
            changeWindowAlpha(activity, 1f, 0.7f);
        else
            changeWindowAlpha(activity, 0.7f, 1f);
    }
    private static void changeWindowAlpha(Activity activity, float startAlpha, float endAlpha){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startAlpha, endAlpha)
                .setDuration(300);
        valueAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            WindowUtil.changeWindowAlpha(activity, value);
        });
        valueAnimator.start();
    }
    private static void changeWindowAlpha(Activity activity, float alpha){
        if(activity == null || alpha < 0 || alpha > 1.0f)
            return;
        Window window = activity.getWindow();
        if(window == null)
            return;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.setAttributes(lp);
    }
}
