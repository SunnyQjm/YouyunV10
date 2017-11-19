package com.sunny.youyun.wifidirect.activity.single_trans.adapter;

import android.support.annotation.Nullable;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.wifidirect.model.DeviceInfo;

import java.util.List;

/**
 * Created by Sunny on 2017/8/11 0011.
 */

public class PeersAdapter extends BaseQuickAdapter<DeviceInfo, BaseViewHolder>{

    public PeersAdapter(@Nullable final List<DeviceInfo> data) {
        super(R.layout.peers_info_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceInfo item) {
        helper.setText(R.id.tv_content, item.getDeviceName() + "\nMax: " + item.getMac());
    }
}
