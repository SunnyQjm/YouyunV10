package com.sunny.youyun.activity.person_info.dynamic_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_info.adapter.DynamicAdapter;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.model.Dynamic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class DynamicFragment extends MVPBaseFragment<DynamicPresenter> implements DynamicContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private DynamicAdapter adapter;
    private List<Dynamic> mList = null;

    private View view = null;

    @Override
    public void onResume() {
        super.onResume();
        //重新显示的时候更新数据
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static DynamicFragment newInstance() {

        Bundle args = new Bundle();

        DynamicFragment fragment = new DynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_upload_record, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView(container);
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    private void initView(ViewGroup container) {
        mList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mList.add(new Dynamic.Builder()
                    .avatar("http://img4.imgtn.bdimg.com/it/u=2880820503,781549093&fm=27&gp=0.jpg")
                    .date(System.currentTimeMillis())
                    .fileName("谁的青春不迷茫")
                    .mode(0)
                    .build());
        }
        adapter = new DynamicAdapter(mList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);

    }

    @Override
    public void showSuccess(String info) {
        super.showSuccess(info);
    }

    @Override
    public void showError(String info) {
        super.showError(info);
    }

    @Override
    public void showTip(String info) {
        super.showTip(info);
    }

    @Override
    protected DynamicPresenter onCreatePresenter() {
        return new DynamicPresenter(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
