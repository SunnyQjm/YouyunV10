package com.sunny.youyun.activity.person_file_manager.adapter;

import android.support.annotation.Nullable;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_file_manager.item.PathItem;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

public class PathAdapter extends BaseQuickAdapter<PathItem, BaseViewHolder>{
    public PathAdapter(@Nullable List<PathItem> data) {
        super(R.layout.path_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PathItem item) {
        helper.setText(R.id.tv_path, item.getPath());
    }
}
