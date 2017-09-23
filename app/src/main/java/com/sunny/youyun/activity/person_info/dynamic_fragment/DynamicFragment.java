package com.sunny.youyun.activity.person_info.dynamic_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_info.adapter.DynamicAdapter;
import com.sunny.youyun.base.fragment.MVPBaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class DynamicFragment extends MVPBaseFragment<DynamicPresenter> implements DynamicContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private DynamicAdapter adapter;
    private int page = 1;
    private View view = null;

    @Override
    public void onResume() {
        super.onResume();
        //重新显示的时候更新数据
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static DynamicFragment newInstance() {

        Bundle args = new Bundle();

        DynamicFragment fragment = new DynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_upload_record, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    private void initView() {
        adapter = new DynamicAdapter(mPresenter.getData());
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        mPresenter.getDynamic(page, true);
    }


    @Override
    protected DynamicPresenter onCreatePresenter() {
        return new DynamicPresenter(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getDynamicSuccess() {
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
}
