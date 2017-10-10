package com.sunny.youyun.activity.person_info.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.GlideUtils;

import java.util.List;

/**
 * Created by Sunny on 2017/10/10 0010.
 */

public class PublicShareFileAdapter extends BaseQuickAdapter<InternetFile, BaseViewHolder>{
    public PublicShareFileAdapter(@Nullable List<InternetFile> data) {
        super(R.layout.collection_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InternetFile item) {
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_description, item.getDescription());
        GlideUtils.setImage(mContext, ((ImageView)helper.getView(R.id.img_icon)), item);
    }
}
