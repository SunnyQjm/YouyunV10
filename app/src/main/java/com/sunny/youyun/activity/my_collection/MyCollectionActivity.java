package com.sunny.youyun.activity.my_collection;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.my_collection.adapter.CollectionAdapter;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;

@Router(IntentRouter.MyCollectionActivity)
public class MyCollectionActivity extends BaseRecyclerViewActivity<MyCollectionPresenter> implements MyCollectionContract.View {

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
    }

    @Override
    protected void onRefreshBegin() {
        page = 1;
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

    private void updateAll() {
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }
}
