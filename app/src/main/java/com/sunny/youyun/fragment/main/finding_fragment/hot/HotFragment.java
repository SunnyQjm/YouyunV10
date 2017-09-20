package com.sunny.youyun.fragment.main.finding_fragment.hot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;
import com.sunny.youyun.fragment.main.finding_fragment.adapter.FindingItemAdapter;

public class HotFragment extends BaseRecyclerViewFragment<HotPresenter> implements HotContract.View {

    private View view = null;
    private FindingItemAdapter adapter;

    public static HotFragment newInstance() {
        HotFragment fragment = new HotFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);
        init();
        return view;
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void loadData() {

    }

    private void init() {
        adapter = new FindingItemAdapter(mPresenter.getDatas());
        adapter.bindToRecyclerView(recyclerView);
    }

    @Override
    protected void onRefreshBegin() {

    }

    @Override
    protected void OnRefreshBeginSync() {

    }

    @Override
    protected void OnRefreshFinish() {

    }

    @Override
    protected void onLoadBeginSync() {

    }

    @Override
    protected void onLoadFinish() {

    }

    @Override
    protected HotPresenter onCreatePresenter() {
        return new HotPresenter(this);
    }
}