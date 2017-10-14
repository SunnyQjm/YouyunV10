package com.sunny.youyun.views.popupwindow.directory_select;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

/**
 * Created by Sunny on 2017/10/14 0014.
 */

public enum  DirectorySelectManager {
    INSTANCE;
    private DirectSelectPopupWindow popupWindow = null;
    public static DirectorySelectManager getInstance(@NonNull final AppCompatActivity context){
        INSTANCE.popupWindow = new DirectSelectPopupWindow(context);
        return INSTANCE;
    }

    /**
     * 设置类型
     * @param type
     * @return
     */
    public DirectorySelectManager setType(int type){
        if(popupWindow != null)
            popupWindow.setType(type);
        return this;
    }

    /**
     * 设置回调监听
     * @param onDismissListener
     * @return
     */
    public DirectorySelectManager setOnDismissListener(
            DirectSelectPopupWindow.OnDismissListener onDismissListener){
        if(popupWindow != null)
            popupWindow.setOnDismissListener(onDismissListener);
        return this;
    }

    public void show(View parent){
        if(popupWindow != null)
            popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }


}
