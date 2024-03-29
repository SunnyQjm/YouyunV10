package com.sunny.youyun.fragment.main.finding_fragment.concern;

import android.os.Bundle;
import android.view.View;

import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.fragment.main.finding_fragment.adapter.FindingItemAdapter;
import com.sunny.youyun.fragment.main.finding_fragment.base.FindingBaseFragment;
import com.sunny.youyun.utils.idling.EspressoIdlingResource;

public class ConcernFragment extends FindingBaseFragment<ConcernPresenter>
        implements ConcernContract.View, BaseQuickAdapter.OnItemClickListener {

    // TODO: Rename and change types and number of parameters
    public static ConcernFragment newInstance() {
        ConcernFragment fragment = new ConcernFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ConcernPresenter onCreatePresenter() {
        return new ConcernPresenter(this);
    }


    @Override
    protected void loadData(boolean isRefresh) {
        if(isRefresh){
            page = 1;
        } else {
            page++;
        }
        mPresenter.getConcernPeopleShares(page, isRefresh);
    }

    @Override
    protected void init() {
        adapter = new FindingItemAdapter(mPresenter.getDatas());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
    }


    @Override
    public void getDatasOnlineSuccess() {
        EspressoIdlingResource.getInstance()
                .decrement();
        updateAll();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        super.onItemClick(adapter, view, position);
    }

}
