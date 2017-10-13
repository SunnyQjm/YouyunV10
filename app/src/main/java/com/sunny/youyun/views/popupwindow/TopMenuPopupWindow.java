package com.sunny.youyun.views.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseStringAdapter;
import com.sunny.youyun.utils.DensityUtil;

import java.util.List;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

public class TopMenuPopupWindow extends PopupWindow {
    private final Context context;
    private ListView listView = null;
    private final List<String> mList;
    private AdapterView.OnItemClickListener onItemClickListener;

    private LayoutInflater inflater;
    private View menuView;
    private BaseStringAdapter adapter;


    public TopMenuPopupWindow(@NonNull Context context,
                              AdapterView.OnItemClickListener onItemClickListener, List<String> mList) {
        this.context = context;
        this.mList = mList;
        this.onItemClickListener = onItemClickListener;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null)
            menuView = inflater.inflate(R.layout.person_file_info_manager_options_view,
                    null, false);
        initView();
    }

    private void initView() {
        listView = (ListView) menuView.findViewById(R.id.listView);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setDividerHeight(DensityUtil.dip2px(context, 0.5f));
        adapter = new BaseStringAdapter(context, mList);
        listView.setAdapter(adapter);

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
        showAsDropDown(view);
    }
}
