package com.sunny.youyun.utils;

import android.content.Context;
import android.widget.TimePicker;

/**
 * Created by Sunny on 2017/4/8 0008.
 */

public class TimePickerUtils {
    public static TimePicker timePicker = null;

    /**
     * 绑定
     * @param context
     */
    public static void bind(Context context){
        timePicker = new TimePicker(context);
    }

    public static TimePicker getInstance(){
        return timePicker;
    }
    /**
     * 解绑
     */
    public static void unBind(){
        if(timePicker != null){
            timePicker = null;
        }
    }
}
