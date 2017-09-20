package com.sunny.youyun.fragment.main.finding_fragment.concern;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;
import com.sunny.youyun.fragment.main.finding_fragment.adapter.FindingItemAdapter;

public class ConcernFragment extends BaseRecyclerViewFragment<ConcernPresenter> implements ConcernContract.View {

    private View view = null;
    private FindingItemAdapter adapter;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

}
