package com.sunny.youyun.activity.my_collection;

import android.os.Bundle;

import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;

public class MyCollectionActivity extends BaseRecyclerViewActivity<MyCollectionPresenter> implements MyCollectionContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
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
