package com.sunny.youyun.activity.person_info.dynamic_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_info.adapter.DynamicAdapter;
import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class DynamicFragment extends BaseRecyclerViewFragment<DynamicPresenter> implements DynamicContract.View {

    private DynamicAdapter adapter;
    private int page = 1;
    private View view = null;
    private boolean isSelf = true;

    @Override
    public void onResume() {
        super.onResume();
        //重新显示的时候更新数据
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    private void setSelt(boolean isSelf){
        this.isSelf = isSelf;
    }
    @Override
    public void onStart() {
        super.onStart();
        if(!isFirst)
            loadData(true);
    }

    public static DynamicFragment newInstance(boolean isSelf) {

        Bundle args = new Bundle();
        DynamicFragment fragment = new DynamicFragment();
        fragment.setArguments(args);
        fragment.setSelt(isSelf);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = super.onCreateView(inflater, container, savedInstanceState);
            initView();
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
        if(!isVisible || !isPrepared)
            return;
        if(isFirst){
            loadData(true);
            isFirst = false;
        }
    }

    private void loadData(boolean isRefresh){
        if(isRefresh){
            page = 1;
            if(isSelf){
                mPresenter.getDynamic(page, true);
            }
        } else {
            page++;
            if(isSelf){
                mPresenter.getDynamic(page, false);
            }
        }
    }
    private void initView() {
        adapter = new DynamicAdapter(mPresenter.getData());
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        refreshLayout.setRefreshAble(false);
        refreshLayout.setLoadAble(false);
        loadData(true);
    }


    @Override
    protected DynamicPresenter onCreatePresenter() {
        return new DynamicPresenter(this);
    }


    @Override
    protected void onRefreshBegin() {
    }

    @Override
    protected void OnRefreshBeginSync() {
        loadData(true);
    }

    @Override
    protected void OnRefreshFinish() {
        getDynamicSuccess();
    }

    @Override
    protected void onLoadBeginSync() {
        loadData(false);
    }

    @Override
    protected void onLoadFinish() {
        getDynamicSuccess();
    }

    @Override
    public void getDynamicSuccess() {
        updateAll();
    }

    private void updateAll() {
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
}
