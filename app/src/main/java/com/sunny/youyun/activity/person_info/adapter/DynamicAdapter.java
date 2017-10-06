package com.sunny.youyun.activity.person_info.adapter;

import android.support.annotation.Nullable;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.data_item.Dynamic;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.TimeUtils;

import java.util.List;

/**
 * Created by Sunny on 2017/9/10 0010.
 */

public class DynamicAdapter extends BaseQuickAdapter<Dynamic, BaseViewHolder> {
    public DynamicAdapter(@Nullable List<Dynamic> data) {
        super(R.layout.person_info_dynamic_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Dynamic item) {
        if (item.getFile() == null)
            return;
        helper.setText(R.id.tv_content, getString(item.getType()) + item.getFile().getName())
                .setText(R.id.tv_date, TimeUtils.returnTime_ymd(item.getCreateTime()));
        GlideUtils.load(mContext, helper.getView(R.id.img_icon), UserInfoManager.getInstance().getUserInfo()
                .getAvatar());
    }

    private String getString(int type) {
        switch (type) {
            case 1:
                return mContext.getString(R.string.already_collect);
            case 3:
                return mContext.getString(R.string.already_upload);
            case 4:
                return mContext.getString(R.string.already_download);
            default:
            case 2:
                return mContext.getString(R.string.already_share);
        }
    }
}
