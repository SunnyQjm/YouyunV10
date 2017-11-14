package com.sunny.youyun.activity.person_info.other_file_fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

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

    @Override
    protected void loadData(boolean isRefresh) {
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        mPresenter.getFiles(userId, page, isRefresh);
    }

    @Override
    protected void init() {
        adapter = new PublicShareFileAdapter(mPresenter.getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(activity,
                DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
        refreshLayout.setLoadAble(true);
        refreshLayout.setRefreshAble(false);
        if (userId < 0)
            isFirst = false;
        loadData(true);
    }

    @Override
    public void getOtherPublicFilesSuccess() {
        updateAll();
    }

    /**
     * 复写父函数，不显示尾布局
     */
    @Override
    public void allDataLoadFinish() {
        if (adapter != null && adapter.getFooterLayout() != null) {
            adapter.getFooterLayout().setVisibility(View.VISIBLE);
        }

        //设置不可加载更多
        refreshLayout.setLoadAble(false);
        updateAll();
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
