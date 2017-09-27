package com.sunny.youyun.activity.person_file_manager.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.nodes.ClassificationNode;
import com.sunny.youyun.utils.GlideUtils;

import java.util.List;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

public class ClassificationAdapter extends BaseQuickAdapter<ClassificationNode, BaseViewHolder> {
    public ClassificationAdapter(@Nullable List<ClassificationNode> data) {
        super(R.layout.classification_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassificationNode item) {
        helper.setText(R.id.tv_name, item.getName());
        GlideUtils.load(mContext, ((ImageView) helper.getView(R.id.img_icon)), item.getIconRes());
    }
}
