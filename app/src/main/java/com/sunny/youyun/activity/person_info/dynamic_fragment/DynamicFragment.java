package com.sunny.youyun.activity.person_info.dynamic_fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_info.adapter.DynamicAdapter;
import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class DynamicFragment extends BaseRecyclerViewFragment<DynamicPresenter> implements DynamicContract.View {

    private boolean isSelf = true;
    private int userId = -1;
    private void setSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isFirst)
            loadData(true);
    }

    public static DynamicFragment newInstance(int userId) {

        Bundle args = new Bundle();
        DynamicFragment fragment = new DynamicFragment();
        fragment.setArguments(args);
        if(userId > 0){
            fragment.setSelf(false);
            fragment.setUserId(userId);
        }else {
            fragment.setSelf(true);
        }

        return fragment;
    }

    @Override
    protected void loadData(boolean isRefresh) {
        if (isRefresh)
            page = 1;
        else
            page++;
        if (isSelf) {
            mPresenter.getDynamic(page, isRefresh);
        } else {
            mPresenter.getDynamic(userId, page, isRefresh);
            refreshLayout.setLoadAble(false);
        }
    }

    @Override
    protected void init() {
        adapter = new DynamicAdapter(mPresenter.getData());
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        refreshLayout.setRefreshAble(false);
        refreshLayout.setLoadAble(true);
        loadData(true);
    }


    @Override
    protected DynamicPresenter onCreatePresenter() {
        return new DynamicPresenter(this);
    }

    @Override
    public void getDynamicSuccess() {
        updateAll();
    }

    @Override
    public void allDataGetFinish() {
        allDataLoadFinish();
    }

}
