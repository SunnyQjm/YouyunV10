package com.sunny.youyun.activity.file_detail_online.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.data_item.Comment;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.TimeUtils;

import java.util.List;

/**
 * 评论Item适配器
 *
 * Created by Sunny on 2017/9/13 0013.
 */

public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {
    public CommentAdapter(@Nullable List<Comment> data) {
        super(R.layout.file_detail_comment_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        //如果评论用户不存在
        if(item.getUser() == null)
            return;
        helper.setText(R.id.tv_name, item.getUser().getUsername())
                .setText(R.id.tv_date, TimeUtils.returnTime_y4md(item.getCreateTime()))
                .setText(R.id.tv_comment, item.getComment())
                .addOnClickListener(R.id.img_icon);

        GlideUtils.load(mContext, ((ImageView)helper.getView(R.id.img_icon)), item.getUser().getAvatar());
    }
}
