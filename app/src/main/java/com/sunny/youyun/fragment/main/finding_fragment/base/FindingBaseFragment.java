package com.sunny.youyun.fragment.main.finding_fragment.base;

import android.content.Intent;
import android.view.View;

import com.sunny.youyun.App;
import com.sunny.youyun.activity.file_detail_online.FileDetailOnlineActivity;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.UUIDUtil;
import com.sunny.youyun.utils.bus.ObjectPool;

/**
 * Created by Sunny on 2017/10/12 0012.
 */

public abstract class FindingBaseFragment<T extends BasePresenter> extends BaseRecyclerViewFragment<T>
        implements BaseView {
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        InternetFile internetFile = (InternetFile) adapter.getItem(position);
        if (internetFile == null)
            return;
        String uuid = UUIDUtil.getUUID();
        ObjectPool.getInstance()
                .put(uuid, internetFile);
        Intent intent = new Intent(activity, FileDetailOnlineActivity.class);
        intent.putExtra("uuid", uuid);
        RouterUtils.openForResult(this, intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
