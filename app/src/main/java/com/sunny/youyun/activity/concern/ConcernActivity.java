package com.sunny.youyun.activity.concern;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_info.adapter.UserItemAdapter;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;

@Router(IntentRouter.ConcernActivity)
public class ConcernActivity extends BaseRecyclerViewActivity<ConcernPresenter> implements ConcernContract.View, EasyRefreshLayout.OnRefreshListener, EasyRefreshLayout.OnLoadListener {

    private UserItemAdapter adapter = null;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onRefreshBegin() {
        page = 1;
    }

    @Override
    protected void OnRefreshBeginSync() {
        //TODO Refresh data here
        mPresenter.getFollowingList(page, true);
    }

    @Override
    protected void OnRefreshFinish() {
        getFollowingSuccess();
    }

    @Override
    protected void onLoadBeginSync() {
        //TODO Load data here
        page++;
        mPresenter.getFollowingList(page, false);
    }

    @Override
    protected void onLoadFinish() {
        getFollowingSuccess();
    }
    private void init() {
        easyBar.setTitle(getString(R.string.my_concern));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });
        adapter = new UserItemAdapter(mPresenter.getData());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);

        refreshLayout.post(() -> {
            refreshLayout.refresh(true);
        });
    }

    @Override
    protected ConcernPresenter onCreatePresenter() {
        return new ConcernPresenter(this);
    }

    @Override
    public void getFollowingSuccess() {
        updateAll();
    }

    private void updateAll() {
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }
}
