package com.sunny.youyun.activity.my_collection;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.my_collection.adapter.CollectionAdapter;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.model.data_item.Collection;
import com.sunny.youyun.model.InternetFile;

import java.util.ArrayList;
import java.util.List;

@Router(IntentRouter.MyCollectionActivity)
public class MyCollectionActivity extends BaseRecyclerViewActivity<MyCollectionPresenter> implements MyCollectionContract.View {

    private CollectionAdapter adapter;
    private List<Collection> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add(new Collection.Builder()
                    .internetFile(new InternetFile.Builder()
                            .description("来自优云的分享")
                            .name("梦想天空分外蓝.mp3")
                            .build())
                    .build());
        }
        adapter = new CollectionAdapter(mList);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        easyBar.setTitle(getString(R.string.my_collection));
    }

    @Override
    protected void onRefreshBegin() {

    }

    @Override
    protected void OnRefreshBeginSync() {

    }

    @Override
    protected void OnRefreshFinish() {

    }

    @Override
    protected void onLoadBeginSync() {

    }

    @Override
    protected void onLoadFinish() {

    }

    @Override
    protected MyCollectionPresenter onCreatePresenter() {
        return new MyCollectionPresenter(this);
    }
}
