package com.sunny.youyun.activity.person_info.other_file_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_info.adapter.PublicShareFileAdapter;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;
import com.sunny.youyun.model.InternetFile;

/**
 * Created by Sunny on 2017/10/10 0010.
 */

public class OtherPublicShareFileFragment extends BaseRecyclerViewFragment<OtherPublicShareFilePresenter>
        implements OtherPublicShareFileContract.View, BaseQuickAdapter.OnItemClickListener {

    private PublicShareFileAdapter adapter;
    private View view = null;
    private int page = 1;
    private int userId = -1;

    private void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isFirst) {
            loadData(true);
        }
    }

    public static OtherPublicShareFileFragment newInstance(int userId) {
        Bundle args = new Bundle();
        OtherPublicShareFileFragment fragment = new OtherPublicShareFileFragment();
        fragment.setArguments(args);
        fragment.setUserId(userId);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        if (!isVisible || !isPrepared)
            return;
        if (isFirst) {
            loadData(true);
            isFirst = false;
        }
    }

    private void loadData(boolean isRefresh) {
        if (isRefresh) {
            page = 1;
            if (endView != null)
                endView.setVisibility(View.INVISIBLE);
            refreshLayout.setLoadAble(true);
        } else {
            page++;
        }
        mPresenter.getFiles(userId, page, isRefresh);
    }

    private void initView() {
        adapter = new PublicShareFileAdapter(mPresenter.getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(activity,
                DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        adapter.setOnItemClickListener(this);
        refreshLayout.setLoadAble(true);
        refreshLayout.setRefreshAble(false);
        loadData(true);
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
        getOtherPublicFilesSuccess();
    }

    @Override
    protected void onLoadBeginSync() {
        loadData(false);
    }

    @Override
    protected void onLoadFinish() {
        getOtherPublicFilesSuccess();
    }

    @Override
    public void getOtherPublicFilesSuccess() {
        updateAll();
        if(adapter.getData().size() == 0)
            refreshLayout.setLoadAble(false);
    }

    @Override
    public void allDataGetFinish() {
        if (endView == null) {
            endView = LayoutInflater.from(activity)
                    .inflate(R.layout.easy_refresh_end, null, false);
            if (adapter != null) {
                adapter.addFooterView(endView);
            }
        } else
            endView.setVisibility(View.VISIBLE);
        //设置不可加载更多
        refreshLayout.setLoadAble(false);
    }

    private void updateAll() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        InternetFile concernItem = (InternetFile) adapter.getItem(position);
    }

    @Override
    protected OtherPublicShareFilePresenter onCreatePresenter() {
        return new OtherPublicShareFilePresenter(this);
    }
}
