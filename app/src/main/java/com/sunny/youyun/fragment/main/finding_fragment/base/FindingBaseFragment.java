package com.sunny.youyun.fragment.main.finding_fragment.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_detail_online.FileDetailOnlineActivity;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;
import com.sunny.youyun.fragment.main.finding_fragment.adapter.FindingItemAdapter;
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
    protected View view = null;
    protected FindingItemAdapter adapter;
    protected int page = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("onCreateView");
        if (view == null) {
            view = super.onCreateView(inflater, container, savedInstanceState);
            init();
        } else {
            super.onCreateView(inflater, container, savedInstanceState);
        }
        isPrepared = true;
        return view;
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void loadData() {
        if (!isVisible || !isPrepared)
            return;
        if (isFirst) {
            loadData(true);
            isFirst = false;
        }
    }

    @Override
    protected void onRefreshBegin() {
        if (endView != null)
            adapter.removeFooterView(endView);
        refreshLayout.setLoadAble(true);
    }

    @Override
    protected void OnRefreshBeginSync() {
        loadData(true);
    }

    @Override
    protected void onLoadBeginSync() {
        loadData(false);
    }

    protected void updateAll() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public void allDataLoadFinish() {
        if (endView == null) {
            endView = LayoutInflater.from(activity)
                    .inflate(R.layout.easy_refresh_end, null, false);
        }
        if (adapter != null) {
            adapter.addFooterView(endView);
        }

        //设置不可加载更多
        refreshLayout.setLoadAble(false);
    }

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

    protected abstract void loadData(boolean isRefresh);

    protected abstract void init();
}
