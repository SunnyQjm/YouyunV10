package com.sunny.youyun.activity.star;

import android.os.Bundle;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.star.adapter.StarRecordAdapter;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.views.EasyBar;

@Router(IntentRouter.StarRecordActivity)
public class StarRecordActivity extends BaseRecyclerViewActivity<StarRecordPresenter>
        implements StarRecordContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void loadData(boolean isRefresh) {
        mPresenter.getStarRecord(page, isRefresh);
    }

    private void init() {
        easyBar.setTitle(getString(R.string.star_record));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });
        adapter = new StarRecordAdapter(mPresenter.getDatas());
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(this,
                RecyclerViewDividerItem.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        loadData(true);
    }

    @Override
    protected StarRecordPresenter onCreatePresenter() {
        return new StarRecordPresenter(this);
    }

    @Override
    public void getStarRecordSuccess() {
        updateAll();
    }
}
