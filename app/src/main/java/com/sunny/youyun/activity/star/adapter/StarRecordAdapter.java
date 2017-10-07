package com.sunny.youyun.activity.star.adapter;

import android.support.annotation.Nullable;

import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.data_item.StarRecord;

import java.util.List;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

public class StarRecordAdapter extends BaseQuickAdapter<StarRecord, BaseViewHolder>{
    public StarRecordAdapter(@Nullable List<StarRecord> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StarRecord item) {

    }
}
