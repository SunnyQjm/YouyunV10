package com.sunny.youyun.activity.person_info.concern_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_info.adapter.UserItemAdapter;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Sunny on 2017/6/25 0025.
 */
public class ConcernFragment extends MVPBaseFragment<ConcernPresenter> implements ConcernContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private UserItemAdapter adapter = null;
    private List<User> mList;
    private View view = null;

    @Override
    public void onResume() {
        super.onResume();
        //重新显示的时候更新数据
        if (adapter != null)
            adapter.notifyDataSetChanged();
        //开始监听
        mPresenter.beginListen();
    }

    public static ConcernFragment newInstance() {
        Bundle args = new Bundle();
        ConcernFragment fragment = new ConcernFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_download_record, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView(container);
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    private void initView(ViewGroup container) {
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add(new User.Builder()
                    .username("SunnyQjm")
                    .avatar("http://img4.imgtn.bdimg.com/it/u=2880820503,781549093&fm=27&gp=0.jpg")
                    .description("简介：普天之下，莫非王土，率土之滨，莫非王臣\n" +
                            "普天之下，莫非王土，率土之滨，莫非王臣\n" +
                            "普天之下，莫非王土，率土之滨，莫非王臣\n" +
                            "普天之下，莫非王土，率土之滨，莫非王臣")
                    .build());
        }
        adapter = new UserItemAdapter(mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        mPresenter.beginListen();

    }

    @Override
    protected ConcernPresenter onCreatePresenter() {
        return new ConcernPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
