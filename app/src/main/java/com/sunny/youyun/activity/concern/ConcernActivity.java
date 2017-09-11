package com.sunny.youyun.activity.concern;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_info.adapter.UserItemAdapter;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.model.User;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;

import java.util.ArrayList;
import java.util.List;

@Router(IntentRouter.ConcernActivity)
public class ConcernActivity extends BaseRecyclerViewActivity<ConcernPresenter> implements ConcernContract.View, EasyRefreshLayout.OnRefreshListener, EasyRefreshLayout.OnLoadListener {

    private UserItemAdapter adapter = null;
    private List<User> mList;
    private View endView;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onRefreshBegin() {
        if(endView != null)
            endView.setVisibility(View.INVISIBLE);
//        adapter.notifyDataSetChanged();
    }

    @Override
    protected void OnRefreshBeginSync() {
        //TODO Refresh data here
        refreshLayout.setAllDataLoadFinish(false);
        count = 0;
        mList.clear();
        for (int i = 0; i < 5; i++) {
            mList.add(new User.Builder()
                    .username("SunnyQjm")
                    .avatar("http://img4.imgtn.bdimg.com/it/u=2880820503,781549093&fm=27&gp=0.jpg")
                    .description("简介：普天之下，莫非王土，率土之滨，莫非王臣\n" +
                            "普天之下，莫非王土，率土之滨，莫非王臣\n" +
                            "普天之下，莫非王土，率土之滨，莫非王臣\n" +
                            "普天之下，莫非王土，率土之滨，莫非王臣")
                    .build());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void OnRefreshFinish() {
        //TODO Update UI after refresh data
        adapter.notifyDataSetChanged();
        //如果数据没有充满，且没有加载完，则加载
        if (!refreshLayout.isAllDataLoadFinish() && !ViewCompat.canScrollVertically(recyclerView, -1)
                && !ViewCompat.canScrollVertically(recyclerView, 1)) {
            refreshLayout.load(true);
        }
    }

    @Override
    protected void onLoadBeginSync() {
        //TODO Load data here
        for (int i = 0; i < 2; i++) {
            count++;
            if (count > 2)
                break;
            mList.add(new User.Builder()
                    .username("SunnyQjm")
                    .avatar("http://img4.imgtn.bdimg.com/it/u=2880820503,781549093&fm=27&gp=0.jpg")
                    .description("简介：普天之下，莫非王土，率土之滨，莫非王臣\n" +
                            "普天之下，莫非王土，率土之滨，莫非王臣\n" +
                            "普天之下，莫非王土，率土之滨，莫非王臣\n" +
                            "普天之下，莫非王土，率土之滨，莫非王臣")
                    .build());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onLoadFinish() {

        //TODO Update UI after load data
        if (count == 2) {
            refreshLayout.setLoadAble(false);
            endView.setVisibility(View.VISIBLE);
            refreshLayout.setAllDataLoadFinish(true);
        }

        //如果数据没有充满，则加载
        if (!refreshLayout.isAllDataLoadFinish() && !ViewCompat.canScrollVertically(recyclerView, -1)
                && !ViewCompat.canScrollVertically(recyclerView, 1)) {
            refreshLayout.load(true);
        }
        adapter.notifyDataSetChanged();
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
        mList = new ArrayList<>();

        adapter = new UserItemAdapter(mList);
        endView = LayoutInflater.from(this).inflate(R.layout.easy_refresh_end, null, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);

        adapter.addFooterView(endView);
        refreshLayout.post(() -> {
            refreshLayout.refresh(true);
        });
    }

    @Override
    protected ConcernPresenter onCreatePresenter() {
        return new ConcernPresenter(this);
    }
}
