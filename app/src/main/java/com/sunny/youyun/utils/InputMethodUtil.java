package com.sunny.youyun.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Sunny on 2017/9/18 0018.
 */

public class InputMethodUtil {

//    public static void showInputMethod
    public static void hide(@NonNull final Context context){
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(im == null)
            return;
         im.toggleSoftInput(0, 0);
    }
}

