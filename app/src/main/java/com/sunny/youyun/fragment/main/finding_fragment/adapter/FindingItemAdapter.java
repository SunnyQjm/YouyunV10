package com.sunny.youyun.fragment.main.finding_fragment.adapter;

import android.support.annotation.Nullable;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.TimeUtils;
import com.sunny.youyun.views.RichText;

import java.util.List;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

public class FindingItemAdapter extends BaseQuickAdapter<InternetFile, BaseViewHolder>{
    public FindingItemAdapter(@Nullable List<InternetFile> data) {
        super(R.layout.finding_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InternetFile item) {
//        if(item == null)
//            return;
        helper.setText(R.id.tv_file_name, item.getName())
                .setText(R.id.tv_file_description, item.getDescription())
                .setText(R.id.rt_view_count, String.valueOf(item.getLookNum()))
                .setText(R.id.rt_down_count, String.valueOf(item.getDownloadCount()))
                .setText(R.id.rt_like_count, String.valueOf(item.getStar()));

        RichText rtLikeNum = helper.getView(R.id.rt_like_count);
        if(!item.isCanStar()){
            rtLikeNum.setDrawableRes(R.drawable.icon_zan_selected);
        } else {
            rtLikeNum.setDrawableRes(R.drawable.icon_zan);
        }
        GlideUtils.setImage(mContext, helper.getView(R.id.img_icon), item);
        //非登录上传
        if(item.getUser() == null){
            helper.setText(R.id.tv_uploader, "佚名上传于" +
                    TimeUtils.returnTime_y4md_line_divide(item.getCreateTime()));
            GlideUtils.load(mContext, helper.getView(R.id.img_avatar), R.drawable.icon_logo_round);
        } else {    //登陆后上传
            helper.setText(R.id.tv_uploader, item.getUser().getUsername() + "上传于" +
                    TimeUtils.returnTime_y4md_line_divide(item.getCreateTime()));
            GlideUtils.load(mContext, helper.getView(R.id.img_avatar), item.getUser().getAvatar());
        }
    }
}
