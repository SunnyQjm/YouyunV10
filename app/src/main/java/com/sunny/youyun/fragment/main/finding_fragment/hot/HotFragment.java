package com.sunny.youyun.fragment.main.finding_fragment.hot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;
import com.sunny.youyun.fragment.main.finding_fragment.adapter.FindingItemAdapter;
import com.sunny.youyun.model.FindingItem;
import com.sunny.youyun.model.InternetFile;

import java.util.ArrayList;
import java.util.List;

public class HotFragment extends BaseRecyclerViewFragment<HotPresenter> implements HotContract.View {

    private View view = null;
    private FindingItemAdapter adapter;

    public static HotFragment newInstance() {
        HotFragment fragment = new HotFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            view = super.onCreateView(inflater, container, savedInstanceState);
            init();
        return view;
    }

    private void init() {
        List<FindingItem> findingItemList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            findingItemList.add(new FindingItem.Builder()
                    .internetFile(new InternetFile.Builder()
                            .name("谁的青春不迷茫.mp4")
                            .description("来自优云的分享")
                            .createTime(System.currentTimeMillis())
                            .build())
                    .build());
        }
        adapter = new FindingItemAdapter(findingItemList);
        adapter.bindToRecyclerView(recyclerView);
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
    protected HotPresenter onCreatePresenter() {
        return new HotPresenter(this);
    }
}