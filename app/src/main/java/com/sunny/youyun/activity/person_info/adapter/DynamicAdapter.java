package com.sunny.youyun.activity.person_info.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.Dynamic;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.TimeUtils;

import java.util.List;

/**
 * Created by Sunny on 2017/9/10 0010.
 */

public class DynamicAdapter extends BaseQuickAdapter<Dynamic, BaseViewHolder>{
    public DynamicAdapter(@Nullable List<Dynamic> data) {
        super(R.layout.person_info_dynamic_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Dynamic item) {
        helper.setText(R.id.tv_content, "分享了" + item.getFileName())
                .setText(R.id.tv_date, TimeUtils.returnTime_ymd(item.getDate()));
        GlideUtils.loadUrl(mContext, ((ImageView)helper.getView(R.id.img_avatar)), item.getAvatar());
    }
}