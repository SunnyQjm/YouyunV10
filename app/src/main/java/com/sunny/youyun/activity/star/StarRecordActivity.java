package com.sunny.youyun.activity.star;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.star.adapter.StarRecordAdapter;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.model.data_item.StarRecord;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.EasyBar;

@Router(IntentRouter.StarRecordActivity)
public class StarRecordActivity extends BaseRecyclerViewActivity<StarRecordPresenter>
        implements StarRecordContract.View, BaseQuickAdapter.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void loadData(boolean isRefresh) {
        getMPresenter().getStarRecord(page, isRefresh);
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
        adapter = new StarRecordAdapter(getMPresenter().getDatas());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        loadData(true);
    }

    @NonNull
    @Override
    protected StarRecordPresenter onCreatePresenter() {
        return new StarRecordPresenter(this);
    }

    @Override
    public void getStarRecordSuccess() {
        updateAll();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        StarRecord starRecord = (StarRecord) adapter.getItem(position);
        if (starRecord == null || starRecord.getFile() == null)
            return;
        RouterUtils.openToFileDetailOnline(this, starRecord.getFileId(),
                starRecord.getFile().getIdentifyCode());
    }
}
