package com.sunny.youyun.wifidirect.activity.single_trans.adapter;

import android.net.wifi.p2p.WifiP2pDevice;
import android.support.annotation.Nullable;

import com.sunny.youyun.R;
import com.sunny.youyun.base.BaseQuickAdapter;
import com.sunny.youyun.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Sunny on 2017/8/11 0011.
 */

public class PeersAdapter extends BaseQuickAdapter<WifiP2pDevice, BaseViewHolder>{

    public PeersAdapter(@Nullable final List<WifiP2pDevice> data) {
        super(R.layout.peers_info_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiP2pDevice item) {
        helper.setText(R.id.tv_content, item.deviceName + "\nMax: " + item.deviceAddress);

    }
}
