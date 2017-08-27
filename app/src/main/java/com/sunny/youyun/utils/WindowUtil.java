package com.sunny.youyun.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Sunny on 2017/8/26 0026.
 */

public class WindowUtil {
    public static void changeWindowAlpha(Activity activity, float alpha){
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
