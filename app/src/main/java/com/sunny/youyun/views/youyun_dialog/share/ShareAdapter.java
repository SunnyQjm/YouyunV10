package com.sunny.youyun.views.youyun_dialog.share;

import android.support.annotation.Nullable;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;

import java.util.List;

/**
 * Created by Sunny on 2017/8/25 0025.
 */

public class ShareAdapter extends BaseQuickAdapter<ShareDialog.ShareNode, BaseViewHolder>{

    public ShareAdapter(@Nullable List<ShareDialog.ShareNode> data) {
        super(R.layout.share_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShareDialog.ShareNode item) {
        helper.setImageResource(R.id.img_icon, item.getIcon())
                .setText(R.id.tv_name, item.getName());
    }
}
