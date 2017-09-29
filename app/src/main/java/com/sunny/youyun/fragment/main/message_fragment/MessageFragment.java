package com.sunny.youyun.fragment.main.message_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.fragment.main.message_fragment.adapter.MessageAdapter;
import com.sunny.youyun.fragment.main.message_fragment.item.HeaderItem;
import com.sunny.youyun.fragment.main.message_fragment.item.MessageItem;
import com.sunny.youyun.model.data_item.Message;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class MessageFragment extends MVPBaseFragment<MessagePresenter> implements MessageContract.View, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    EasyRefreshLayout refreshLayout;
    Unbinder unbinder;
    private View view = null;
    private MessageAdapter adapter;

    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_message, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.message));
        easyBar.setLeftIconInVisible();
        create_header_view();
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        adapter = new MessageAdapter(mPresenter.getData());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
    }

    private void create_header_view() {
        mPresenter.getData().add(new HeaderItem(getString(R.string.zan),
                R.drawable.icon_message_zan));
        mPresenter.getData().add(new HeaderItem(getString(R.string.comment),
                R.drawable.icon_message_comment));
        mPresenter.getData().add(new MessageItem(new Message.Builder()
                .content("balabalabala")
                .createTime(System.currentTimeMillis())
                .user(UserInfoManager.getInstance().getUserInfo())));
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
    protected MessagePresenter onCreatePresenter() {
        return new MessagePresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
