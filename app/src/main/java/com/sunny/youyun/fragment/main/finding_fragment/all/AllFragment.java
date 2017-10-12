package com.sunny.youyun.fragment.main.finding_fragment.all;

import android.os.Bundle;

import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.fragment.main.finding_fragment.adapter.FindingItemAdapter;
import com.sunny.youyun.fragment.main.finding_fragment.base.FindingBaseFragment;

public class AllFragment extends FindingBaseFragment<AllPresenter>
        implements AllContract.View, BaseQuickAdapter.OnItemClickListener {

    public static AllFragment newInstance() {
        AllFragment fragment = new AllFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected AllPresenter onCreatePresenter() {
        return new AllPresenter(this);
    }

    @Override
    protected void init() {
        adapter = new FindingItemAdapter(mPresenter.getDatas());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
        mPresenter.getForumDataALL(page, true);
    }

    @Override
    public void getForumDataSuccess() {
        updateAll();
    }

    @Override
    protected void loadData(boolean isRefresh) {
        if(isRefresh){
            page = 1;
        } else {
            page++;
        }
        mPresenter.getForumDataALL(page, isRefresh);
    }

    @Override
    protected void OnRefreshFinish() {

    }

    @Override
    protected void onLoadFinish() {

    }
}
