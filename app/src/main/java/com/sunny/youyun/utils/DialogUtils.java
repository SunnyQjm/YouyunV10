package com.sunny.youyun.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sunny.youyun.R;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class DialogUtils {


    /**
     * 显示选择对话框
     *
     * @param context
     * @param dialog
     * @param listener
     */
    public static void showSelectDialog(Context context, Dialog dialog, final View.OnClickListener listener) {
        Log.d("TAG", "showShareDialog");
        View view = LayoutInflater.from(context).inflate(R.layout.chosing_dialog, null);
        // 设置style 控制默认dialog带来的边距问题
        dialog.setContentView(view);
        dialog.show();

        final TextView et_trans = (TextView) view.findViewById(R.id.et_trans);
        final TextView et_download = (TextView) view.findViewById(R.id.et_download);
        final TextView et_upload = (TextView) view.findViewById(R.id.et_upload);


        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_direct); //获取图片
        drawable.setBounds(DensityUtil.dip2px(context, 10), 0, DensityUtil.dip2px(context, 30), DensityUtil.dip2px(context, 20));  //设置图片参数
        et_trans.setCompoundDrawables(drawable, null, null, null);  //设置到哪个控件的位置（）

        drawable = context.getResources().getDrawable(R.drawable.icon_download); //获取图片
        drawable.setBounds(DensityUtil.dip2px(context, 10), 0, DensityUtil.dip2px(context, 30), DensityUtil.dip2px(context, 20));  //设置图片参数
        et_download.setCompoundDrawables(drawable, null, null, null);  //设置到哪个控件的位置（）

        drawable = context.getResources().getDrawable(R.drawable.icon_upload); //获取图片
        drawable.setBounds(DensityUtil.dip2px(context, 10), 0, DensityUtil.dip2px(context, 30), DensityUtil.dip2px(context, 20));  //设置图片参数
        et_upload.setCompoundDrawables(drawable, null, null, null);  //设置到哪个控件的位置（）

        et_trans.setOnClickListener(listener);
        et_download.setOnClickListener(listener);
        et_upload.setOnClickListener(listener);

        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.verticalMargin = 0.2f;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = DensityUtil.dip2px(context, 210);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        window.setAttributes(params);
    }
}
