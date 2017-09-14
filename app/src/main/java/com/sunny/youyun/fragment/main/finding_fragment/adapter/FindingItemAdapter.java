package com.sunny.youyun.fragment.main.finding_fragment.adapter;

import android.support.annotation.Nullable;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.FindingItem;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.TimeUtils;

import java.util.List;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

public class FindingItemAdapter extends BaseQuickAdapter<FindingItem, BaseViewHolder>{
    public FindingItemAdapter(@Nullable List<FindingItem> data) {
        super(R.layout.finding_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FindingItem item) {
        if(item.getInternetFile() == null)
            return;
        helper.setText(R.id.tv_file_name, item.getInternetFile().getName())
                .setText(R.id.tv_file_description, item.getInternetFile().getDescription())
                .setText(R.id.rt_view_count, String.valueOf(item.getInternetFile().getLookNum()))
                .setText(R.id.rt_down_count, String.valueOf(item.getInternetFile().getDownloadCount()))
                .setText(R.id.rt_like_count, String.valueOf(item.getInternetFile().getStar()));

        GlideUtils.setImage(mContext, helper.getView(R.id.img_icon), item.getInternetFile());
        //非登录上传
        if(item.getUser() == null){
            helper.setText(R.id.tv_uploader, "佚名上传于" +
                    TimeUtils.returnTime_y4md_line_divide(item.getInternetFile().getCreateTime()));
            GlideUtils.load(mContext, helper.getView(R.id.img_avatar), R.drawable.icon_logo_round);
        } else {    //登陆后上传
            helper.setText(R.id.tv_uploader, item.getUser().getUsername() + "上传于" +
                    TimeUtils.returnTime_y4md_line_divide(item.getInternetFile().getCreateTime()));
            GlideUtils.load(mContext, helper.getView(R.id.img_avatar), item.getUser().getAvatar());
        }
    }
}
