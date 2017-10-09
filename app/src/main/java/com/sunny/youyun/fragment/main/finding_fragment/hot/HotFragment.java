package com.sunny.youyun.fragment.main.finding_fragment.hot;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.activity.file_detail_online.FileDetailOnlineActivity;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;
import com.sunny.youyun.fragment.main.finding_fragment.adapter.FindingItemAdapter;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.UUIDUtil;
import com.sunny.youyun.utils.bus.ObjectPool;

public class HotFragment extends BaseRecyclerViewFragment<HotPresenter> implements HotContract.View, BaseQuickAdapter.OnItemClickListener {

    private View view = null;
    private FindingItemAdapter adapter;
    private int page;

    public static HotFragment newInstance() {
        HotFragment fragment = new HotFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null){
            view = super.onCreateView(inflater, container, savedInstanceState);
            init();
        } else{
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
        if(!isVisible || !isPrepared)
            return;
        if(isFirst){
            page = 1;
            mPresenter.getForumDataHot(page, true);
            isFirst = false;
        }
    }

    private void init() {
        adapter = new FindingItemAdapter(mPresenter.getDatas());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void onRefreshBegin() {
        page = 1;
    }

    @Override
    protected void OnRefreshBeginSync() {
        mPresenter.getForumDataHot(page, true);
    }

    @Override
    protected void OnRefreshFinish() {
        getDataSuccess();
    }

    @Override
    protected void onLoadBeginSync() {
        page++;
        mPresenter.getForumDataHot(page, false);
    }

    @Override
    protected void onLoadFinish() {
        getDataSuccess();
    }

    @Override
    protected HotPresenter onCreatePresenter() {
        return new HotPresenter(this);
    }

    @Override
    public void getDataSuccess() {
        updateAll();
    }

    @Override
    public void allDataLoadFinish() {
        refreshLayout.setLoadAble(false);
    }

    private void updateAll() {
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        InternetFile internetFile = (InternetFile) adapter.getItem(position);
        if(internetFile == null)
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
        if(requestCode == 0 && adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
}