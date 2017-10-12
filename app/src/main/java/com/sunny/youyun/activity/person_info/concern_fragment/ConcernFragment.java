package com.sunny.youyun.activity.person_info.concern_fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

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
public class ConcernFragment extends BaseRecyclerViewFragment<ConcernPresenter>
        implements ConcernContract.View, BaseQuickAdapter.OnItemClickListener {

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
        if (!isFirst) {
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

    @Override
    protected void loadData(boolean isRefresh) {
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        if (isSelf) {
            mPresenter.getFollowingList(page, true);
        } else {
            refreshLayout.setLoadAble(false);
        }
    }

    @Override
    protected void init() {
        adapter = new UserItemAdapter(mPresenter.getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(activity,
                DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        adapter.setOnItemClickListener(this);
        mPresenter.beginListen();
        refreshLayout.setLoadAble(true);
        refreshLayout.setRefreshAble(false);
    }

    @Override
    protected ConcernPresenter onCreatePresenter() {
        return new ConcernPresenter(this);
    }

    @Override
    public void getFollowingListSuccess() {
        updateAll();
    }

    @Override
    public void allDataGetFinish() {
        allDataLoadFinish();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ConcernItem concernItem = (ConcernItem) adapter.getItem(position);
        if (concernItem == null || concernItem.getUser() == null)
            return;
        RouterUtils.open(activity, IntentRouter.PersonInfoActivity,
                String.valueOf(concernItem.getUser().getId()));
    }
}
