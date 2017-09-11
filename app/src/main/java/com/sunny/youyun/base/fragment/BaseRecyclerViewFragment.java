package com.sunny.youyun.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.easy_refresh.ArrowRefreshHeader;
import com.sunny.youyun.views.easy_refresh.EasyRefreshFooter;
import com.sunny.youyun.views.easy_refresh.EasyRefreshFooterHandler;
import com.sunny.youyun.views.easy_refresh.EasyRefreshHeaderHandler;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 自定义的一个Fragment
 * 包含一个title和RecyclerView
 * 并且支持下拉刷新和上拉加载更多
 * Created by Sunny on 2017/8/29 0029.
 */

public abstract class BaseRecyclerViewFragment<P extends BasePresenter> extends MVPBaseFragment<P> {

    protected View view = null;
    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    EasyRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.base_recycler_view_layout, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }

        initView();
        return view;
    }

    private void initView() {
        //设置数据充满布局才允许上拉加载更多
        refreshLayout.setLoadOnlyDataFullScreen(true);
        //设置头布局
        EasyRefreshHeaderHandler easyRefreshHeaderHandler = new ArrowRefreshHeader(R.layout.easy_refresh_header);
        refreshLayout.setHeader(easyRefreshHeaderHandler);
        //设置尾布局
        EasyRefreshFooterHandler easyRefreshFooterHandler = new EasyRefreshFooter(R.layout.easy_refresh_footer);
        refreshLayout.setFooter(easyRefreshFooterHandler);



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
