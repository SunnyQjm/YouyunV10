package com.sunny.youyun.activity.star.adapter;

import android.support.annotation.Nullable;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.data_item.StarRecord;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.TimeUtils;

import java.util.List;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

public class StarRecordAdapter extends BaseQuickAdapter<StarRecord, BaseViewHolder>{
    public StarRecordAdapter(@Nullable List<StarRecord> data) {
        super(R.layout.finding_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StarRecord item) {
        InternetFile file = item.getFile();
        if(file != null){
            helper.setText(R.id.tv_file_name, file.getName())
                    .setText(R.id.tv_file_description, file.getDescription())
                    .setText(R.id.rt_view_count, String.valueOf(file.getLookNum()))
                    .setText(R.id.rt_down_count, String.valueOf(file.getDownloadCount()))
                    .setText(R.id.rt_like_count, String.valueOf(file.getStar()));
            GlideUtils.setImage(mContext, helper.getView(R.id.img_icon), file);
        }
        if(item.getUser() != null){
            helper.setText(R.id.tv_uploader, item.getUser().getUsername() +"赞了这个文件" +
                    TimeUtils.returnTime_y4md_line_divide(item.getCreateTime()));
            GlideUtils.load(mContext, helper.getView(R.id.img_avatar), item.getUser().getAvatar());
        }
    }
}
