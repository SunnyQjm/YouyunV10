package com.sunny.youyun.activity.file_manager.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.adpater.ExpandableItemAdapter;
import com.sunny.youyun.base.MVPBaseFragment;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.base.entity.MyDecoration;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Sunny on 2017/8/5 0005.
 */

public abstract class BaseFileManagerFragment<P extends BasePresenter> extends MVPBaseFragment<P> implements BaseView {
    @LayoutRes
    private int layoutRes = -1;
    protected static final String LAYOUT_RES = "LAYOUT_RES";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private Unbinder unbinder;
    private ExpandableItemAdapter adapter;
    private View view = null;

    public BaseFileManagerFragment(){

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(layoutRes == -1){
            layoutRes = getArguments().getInt(LAYOUT_RES);
        }
        if (view == null) {
            // Inflate the layout for this fragment
            view = inflater.inflate(layoutRes, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
            parent.removeView(view);
        return view;
    }

    protected abstract void refreshData();

    protected abstract List<MultiItemEntity> getData();

    private void initView() {
        refreshLayout.setEnabled(false);
        refreshLayout.setOnRefreshListener(this::refreshData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ExpandableItemAdapter(activity, getData(), mListener);
        recyclerView.addItemDecoration(new MyDecoration(activity, MyDecoration.VERTICAL_LIST));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        refreshData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void updateUI() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            if (refreshLayout != null)
                refreshLayout.setRefreshing(false);
            if (adapter.getData().size() > 0)
                adapter.expand(0);
        }
    }

    @Override
    public void showSuccess(String info) {

    }

    @Override
    public void showError(String info) {

    }

    @Override
    protected P onCreatePresenter() {
        return null;
    }
}
