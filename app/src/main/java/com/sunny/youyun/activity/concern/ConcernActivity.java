package com.sunny.youyun.activity.concern;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_info.adapter.UserItemAdapter;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.model.data_item.ConcernItem;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;

@Router(IntentRouter.ConcernActivity)
public class ConcernActivity extends BaseRecyclerViewActivity<ConcernPresenter>
        implements ConcernContract.View, EasyRefreshLayout.OnRefreshListener,
        EasyRefreshLayout.OnLoadListener, BaseQuickAdapter.OnItemClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void loadData(boolean isRefresh) {
        mPresenter.getFollowingList(page, isRefresh);
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
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(
                this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        adapter.setOnItemClickListener(this);
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

    @Override
    public void allDataGetFinish() {
        System.out.println("allDataGetFinish");
        super.allDataLoadFinish(adapter);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ConcernItem concernItem = (ConcernItem) adapter.getItem(position);
        if(concernItem == null || concernItem.getUser() == null)
            return;
        RouterUtils.open(this, IntentRouter.PersonInfoActivity,
                String.valueOf(concernItem.getUser().getId()));
    }
}
