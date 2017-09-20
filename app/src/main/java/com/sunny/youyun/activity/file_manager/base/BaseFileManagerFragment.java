package com.sunny.youyun.activity.file_manager.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.adpater.ExpandableItemAdapter;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;
import com.sunny.youyun.utils.EasyPermission;

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

    //当前Fragment是否可见
    protected boolean isVisible = false;
    //标志位，标识Fragment是否已经完成初始化
    protected boolean isPrepared = false;
    //是否是第一次
    protected boolean isFirst = true;

    public BaseFileManagerFragment(){

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    /**
     * 下面的函数由系统调用
     * 在Fragment可见时加载数据
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected abstract void onInvisible();

    protected void onVisible(){
        if(!isPrepared)
            return;
        EasyPermission.checkAndRequestREAD_WRITE_EXTENAL(activity, new EasyPermission.OnPermissionRequestListener() {
            @Override
            public void success() {
                //加载数据
                loadData();
            }

            @Override
            public void fail() {

            }
        });
    }

    protected abstract void loadData();

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
        isPrepared = true;
        onVisible();
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
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);

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
