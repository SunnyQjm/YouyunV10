package com.sunny.youyun.activity.my_collection;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.my_collection.adapter.CollectionAdapter;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.model.data_item.Collection;
import com.sunny.youyun.utils.RouterUtils;

@Router(IntentRouter.MyCollectionActivity)
public class MyCollectionActivity extends BaseRecyclerViewActivity<MyCollectionPresenter> implements MyCollectionContract.View, BaseQuickAdapter.OnItemClickListener {

    private CollectionAdapter adapter;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        adapter = new CollectionAdapter(mPresenter.getData());
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(
                this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        easyBar.setTitle(getString(R.string.my_collection));
        mPresenter.getCollections(page, true);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void onRefreshBegin() {
        page = 1;
        refreshLayout.setLoadAble(true);
    }

    @Override
    protected void OnRefreshBeginSync() {
        mPresenter.getCollections(page, true);
    }

    @Override
    protected void OnRefreshFinish() {
    }

    @Override
    protected void onLoadBeginSync() {
        page++;
        mPresenter.getCollections(page, false);
    }

    @Override
    protected void onLoadFinish() {

    }

    @Override
    protected MyCollectionPresenter onCreatePresenter() {
        return new MyCollectionPresenter(this);
    }

    @Override
    public void getCollectionsSuccess() {
        updateAll();
    }

    @Override
    public void allDataLoadFinish() {
        super.allDataGetFinish(adapter);
    }

    private void updateAll() {
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Collection collection = (Collection) adapter.getItem(position);
        if(collection == null || collection.getFile() == null)
            return;
        RouterUtils.openToFileDetailOnline(this, collection.getFile().getId(),
                collection.getFile().getIdentifyCode());
    }
}
