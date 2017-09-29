package com.sunny.youyun.activity.person_info.concern_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_info.adapter.UserItemAdapter;
import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;


/**
 * Created by Sunny on 2017/6/25 0025.
 */
public class ConcernFragment extends BaseRecyclerViewFragment<ConcernPresenter> implements ConcernContract.View {

    private UserItemAdapter adapter = null;
    private View view = null;
    private int page = 1;
    @Override
    public void onResume() {
        super.onResume();
        //重新显示的时候更新数据
        if (adapter != null)
            adapter.notifyDataSetChanged();
        //开始监听
        mPresenter.beginListen();
    }

    public static ConcernFragment newInstance() {
        Bundle args = new Bundle();
        ConcernFragment fragment = new ConcernFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = super.onCreateView(inflater, container, savedInstanceState);
            initView(container);
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
            page = 1;
            mPresenter.getFollowingList(page, true);
            isFirst = false;
        }
    }

    private void initView(ViewGroup container) {
        adapter = new UserItemAdapter(mPresenter.getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        mPresenter.beginListen();

    }

    @Override
    protected ConcernPresenter onCreatePresenter() {
        return new ConcernPresenter(this);
    }

    @Override
    protected void onRefreshBegin() {
        page = 1;
    }

    @Override
    protected void OnRefreshBeginSync() {
        mPresenter.getFollowingList(page, true);
    }

    @Override
    protected void OnRefreshFinish() {
        getFollowingListSuccess();
    }

    @Override
    protected void onLoadBeginSync() {
        page++;
        mPresenter.getFollowingList(page, false);
    }

    @Override
    protected void onLoadFinish() {
        getFollowingListSuccess();
    }

    @Override
    public void getFollowingListSuccess() {
        updateAll();
    }

    private void updateAll() {
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }
}
