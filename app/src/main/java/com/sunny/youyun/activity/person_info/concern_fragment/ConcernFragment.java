package com.sunny.youyun.activity.person_info.concern_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_info.adapter.UserItemAdapter;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;
import com.sunny.youyun.model.data_item.ConcernItem;
import com.sunny.youyun.utils.RouterUtils;


/**
 * Created by Sunny on 2017/6/25 0025.
 */
public class ConcernFragment extends BaseRecyclerViewFragment<ConcernPresenter> implements ConcernContract.View, BaseQuickAdapter.OnItemClickListener {

    private UserItemAdapter adapter = null;
    private View view = null;
    private int page = 1;
    private boolean isSelf = true;
    @Override
    public void onResume() {
        super.onResume();
        //重新显示的时候更新数据
        if (adapter != null)
            adapter.notifyDataSetChanged();
        //开始监听
        mPresenter.beginListen();
    }

    private void setSelf(boolean self) {
        isSelf = self;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isFirst){
            loadData(true);
        }
    }

    public static ConcernFragment newInstance(boolean isSelf) {
        Bundle args = new Bundle();
        ConcernFragment fragment = new ConcernFragment();
        fragment.setArguments(args);
        fragment.setSelf(isSelf);
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
                mPresenter.getFollowingList(page, true);
            }
        }else{
            page++;
            if(isSelf){
                mPresenter.getFollowingList(page, false);
            }
        }
    }

    private void initView() {
        adapter = new UserItemAdapter(mPresenter.getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(activity,
                DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        adapter.setOnItemClickListener(this);
        mPresenter.beginListen();
        refreshLayout.setLoadAble(false);
        refreshLayout.setRefreshAble(false);
    }

    @Override
    protected ConcernPresenter onCreatePresenter() {
        return new ConcernPresenter(this);
    }

    @Override
    protected void onRefreshBegin() {
        endView.setVisibility(View.GONE);
    }

    @Override
    protected void OnRefreshBeginSync() {
        loadData(true);
    }

    @Override
    protected void OnRefreshFinish() {
        getFollowingListSuccess();
    }

    @Override
    protected void onLoadBeginSync() {
        loadData(false);
    }

    @Override
    protected void onLoadFinish() {
        getFollowingListSuccess();
    }

    @Override
    public void getFollowingListSuccess() {
        updateAll();
    }

    @Override
    public void allDataGetFinish() {
        if(endView == null) {
            endView = LayoutInflater.from(activity)
                    .inflate(R.layout.easy_refresh_end, null, false);
            if(adapter != null){
                adapter.addFooterView(endView);
            }
        }
        else
            endView.setVisibility(View.VISIBLE);
        //设置不可加载更多
        refreshLayout.setLoadAble(false);
    }

    private void updateAll() {
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ConcernItem concernItem = (ConcernItem) adapter.getItem(position);
        if(concernItem == null || concernItem.getUser() == null)
            return;
        RouterUtils.open(activity, IntentRouter.PersonInfoActivity,
                String.valueOf(concernItem.getUser().getId()));
    }
}
