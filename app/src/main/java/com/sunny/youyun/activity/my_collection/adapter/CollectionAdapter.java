package com.sunny.youyun.activity.my_collection.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.data_item.Collection;
import com.sunny.youyun.utils.GlideUtils;

import java.util.List;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

public class CollectionAdapter extends BaseQuickAdapter<Collection, BaseViewHolder>{
    public CollectionAdapter(@Nullable List<Collection> data) {
        super(R.layout.collection_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Collection item) {
        helper.setText(R.id.tv_name, item.getInternetFile().getName())
                .setText(R.id.tv_description, item.getInternetFile().getDescription());
        GlideUtils.setImage(mContext, ((ImageView)helper.getView(R.id.img_icon)), item.getInternetFile());
    }
}
