package com.sunny.youyun.activity.person_info.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.User;
import com.sunny.youyun.utils.GlideUtils;

import java.util.List;

/**
 * Created by Sunny on 2017/9/10 0010.
 */

public class UserItemAdapter extends BaseQuickAdapter<User, BaseViewHolder>{
    public UserItemAdapter(@Nullable List<User> data) {
        super(R.layout.person_info_concern_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        helper.setText(R.id.tv_nickname, item.getUsername())
                .setText(R.id.tv_description, item.getDescription());
        GlideUtils.loadUrl(mContext, ((ImageView)helper.getView(R.id.img_avatar)), item.getAvatar());
    }
}
