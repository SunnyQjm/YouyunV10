package com.sunny.youyun.views.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.sunny.youyun.R;
import com.sunny.youyun.views.RichText;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

public class TopMenuPopupWindow extends PopupWindow implements View.OnClickListener {
    private final Context context;
    private RichText rtCreateDirectory;
    private RichText rtUpload;

    private LayoutInflater inflater;
    private View menuView;
    private OnSelectListener listener;

    public TopMenuPopupWindow(@NonNull Context context,
                              OnSelectListener onSelectListener) {
        this.context = context;
        this.listener = onSelectListener;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null)
            menuView = inflater.inflate(R.layout.person_file_info_manager_options_view,
                    null, false);
        initView();
    }

    private void initView() {
        rtCreateDirectory = (RichText) menuView.findViewById(R.id.rt_create_directory);
        rtUpload = (RichText) menuView.findViewById(R.id.rt_upload);
        rtCreateDirectory.setOnClickListener(this);
        rtUpload.setOnClickListener(this);
        //initPopup
        // 设置AccessoryPopup的view
        this.setContentView(menuView);
        // 设置AccessoryPopup弹出窗体的宽度
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置AccessoryPopup弹出窗体的高度
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置AccessoryPopup弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x33000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    public void show(View view) {
        System.out.println("view_width: " + (view.getMeasuredWidth() + view.getPaddingLeft() +
                view.getPaddingRight()));
        System.out.println("width: " + view.getWidth());
        showAsDropDown(view, -view.getMeasuredWidth() - view.getPaddingRight() - 30, 10);
    }


    public interface OnSelectListener {
        void onCreateDirectory();

        void onUpload();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rt_create_directory:
                if (listener != null)
                    listener.onCreateDirectory();
                break;
            case R.id.rt_upload:
                if (listener != null)
                    listener.onUpload();
                break;
        }
    }
}
