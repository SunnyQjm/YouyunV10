package com.sunny.youyun.activity.comment.adapter;

import android.support.annotation.Nullable;

import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.data_item.Comment;

import java.util.List;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

public class CommentRecordAdapter extends BaseQuickAdapter<Comment, BaseViewHolder>{
    public CommentRecordAdapter(@Nullable List<Comment> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {

    }
}
