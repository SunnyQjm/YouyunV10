package com.sunny.youyun.fragment.main.finding_fragment.concern;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.base.fragment.BaseRecyclerViewFragment;
import com.sunny.youyun.fragment.main.finding_fragment.adapter.FindingItemAdapter;
import com.sunny.youyun.model.FindingItem;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.User;

import java.util.ArrayList;
import java.util.List;

public class ConcernFragment extends BaseRecyclerViewFragment<ConcernPresenter> implements ConcernContract.View {

    private View view = null;
    private FindingItemAdapter adapter;

    // TODO: Rename and change types and number of parameters
    public static ConcernFragment newInstance() {
        ConcernFragment fragment = new ConcernFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected ConcernPresenter onCreatePresenter() {
        return new ConcernPresenter(this);
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
                            .name("优云.apk")
                            .description("来自优云的分享")
                            .createTime(System.currentTimeMillis())
                            .build())
                    .user(new User.Builder()
                            .username("Sunny")
                            .avatar("http://img4.imgtn.bdimg.com/it/u=2880820503,781549093&fm=27&gp=0.jpg")
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

}