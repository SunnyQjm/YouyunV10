package com.sunny.youyun.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Sunny on 2017/9/18 0018.
 */

public class InputMethodUtil {

    /**
     * q切换软键盘的状态
     * @param context
     */
    public static void change(@NonNull final Context context){
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if(im == null || !im.isActive())
            return;
        im.toggleSoftInput(0, 0);
    }

    public static void hide(@NonNull final Context context, @NonNull final View view){
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(im == null || !im.isActive())
            return;
        //隐藏软键盘 //
        im.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

