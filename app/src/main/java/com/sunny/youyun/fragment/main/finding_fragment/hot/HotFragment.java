package com.sunny.youyun.fragment.main.finding_fragment.hot;

import android.os.Bundle;

import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.fragment.main.finding_fragment.adapter.FindingItemAdapter;
import com.sunny.youyun.fragment.main.finding_fragment.base.FindingBaseFragment;
import com.sunny.youyun.utils.idling.EspressoIdlingResource;

public class HotFragment extends FindingBaseFragment<HotPresenter>
        implements HotContract.View, BaseQuickAdapter.OnItemClickListener {

    public static HotFragment newInstance() {
        HotFragment fragment = new HotFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void init() {
        adapter = new FindingItemAdapter(mPresenter.getDatas());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected HotPresenter onCreatePresenter() {
        return new HotPresenter(this);
    }

    @Override
    public void getDataSuccess() {
        EspressoIdlingResource.getInstance()
                .decrement();
        updateAll();
    }

    @Override
    protected void loadData(boolean isRefresh) {
        if(isRefresh)
            page = 1;
        else
            page++;
        mPresenter.getForumDataHot(page, isRefresh);
    }

    @Override
    protected void OnRefreshFinish() {

    }

    @Override
    protected void onLoadFinish() {

    }
}