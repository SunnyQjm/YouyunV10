package com.sunny.youyun.activity.comment.adapter;

import android.support.annotation.Nullable;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.data_item.CommentRecord;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.TimeUtils;

import java.util.List;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

public class CommentRecordAdapter extends BaseQuickAdapter<CommentRecord, BaseViewHolder>{
    public CommentRecordAdapter(@Nullable List<CommentRecord> data) {
        super(R.layout.comment_record_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentRecord item) {
        InternetFile file = item.getFile();
        if(file != null){
            helper.setText(R.id.tv_file_name, file.getName())
                    .setText(R.id.tv_file_description, file.getDescription())
                    .setText(R.id.rt_view_count, String.valueOf(file.getLookNum()))
                    .setText(R.id.rt_down_count, String.valueOf(file.getDownloadCount()))
                    .setText(R.id.rt_like_count, String.valueOf(file.getStar()));
            GlideUtils.setImage(mContext, helper.getView(R.id.img_icon), file);
        }
        helper.setText(R.id.tv_comment_content, item.getComment());
        if(item.getUser() != null){
            helper.setText(R.id.tv_uploader, item.getUser().getUsername() +"评论了这个文件" +
                    TimeUtils.returnTime_y4md_line_divide(item.getCreateTime()));
            GlideUtils.load(mContext, helper.getView(R.id.img_avatar), item.getUser().getAvatar());
        }
    }
}
