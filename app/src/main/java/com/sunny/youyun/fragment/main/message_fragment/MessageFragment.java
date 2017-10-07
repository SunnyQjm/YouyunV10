package com.sunny.youyun.fragment.main.message_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.chat.ChatActivity;
import com.sunny.youyun.activity.chat.config.ChatConfig;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.fragment.main.message_fragment.adapter.MessageAdapter;
import com.sunny.youyun.fragment.main.message_fragment.item.HeaderItem;
import com.sunny.youyun.model.data_item.PrivateLetter;
import com.sunny.youyun.model.event.JPushEvent;
import com.sunny.youyun.model.manager.MessageManager;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.bus.MessageEventBus;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.easy_refresh.ArrowRefreshHeader;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class MessageFragment extends MVPBaseFragment<MessagePresenter>
        implements MessageContract.View, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    EasyRefreshLayout refreshLayout;
    Unbinder unbinder;
    private View view = null;
    private MessageAdapter adapter;
    private int page = 1;

    @Override
    public void onStart() {
        super.onStart();
        MessageEventBus.getInstance()
                .register(this);
        updateAll();
        System.out.println("MessageFragment onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        MessageEventBus.getInstance()
                .unregister(this);
        System.out.println("MessageFragment onStop");
    }

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
        System.out.println("onCreateView");
        return view;
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.message));
        easyBar.setLeftIconInVisible();
        create_header_view();
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));
        refreshLayout.setHeader(new ArrowRefreshHeader(R.layout.easy_refresh_header));
        refreshLayout.setOnRefreshListener(() -> {
            page = 1;
            mPresenter.getPrivateLetterList(page, true);
            refreshLayout.closeRefresh();
        });

        refreshLayout.setOnLoadListener(() -> {
            page++;
            mPresenter.getPrivateLetterList(page, false);
            refreshLayout.closeLoad();
        });
        adapter = new MessageAdapter(mPresenter.getData());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
        //一开始先加载一波数据
        mPresenter.getPrivateLetterList(page, true);
    }

    private void create_header_view() {
        mPresenter.getData().add(new HeaderItem(getString(R.string.zan),
                R.drawable.icon_message_zan));
        mPresenter.getData().add(new HeaderItem(getString(R.string.comment),
                R.drawable.icon_message_comment));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(JPushEvent jPushEvent) {
        updateAll();
        switch (jPushEvent.getType()) {
            case JPushEvent.INSTANTCONTACT:
                break;
            case JPushEvent.COMMENT:
                break;
            case JPushEvent.FOLLOW:
                break;
            case JPushEvent.STAR:
                break;
        }
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
        if (position >= 2) {
            PrivateLetter letter = (PrivateLetter) adapter.getItem(position);
            if (letter == null)
                return;
            MessageManager.getInstance().clearCount(letter.getId());
            Intent intent = new Intent(activity, ChatActivity.class);
            intent.putExtra(ChatConfig.PARAM_USER_ID, letter.getId());
            intent.putExtra(ChatConfig.PARAM_USER_NICKNAME, letter.getUsername());
            RouterUtils.openForResult(this, intent, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        page = 1;
        mPresenter.getPrivateLetterList(page, true);
    }

    @Override
    public void getPrivateLetterListSuccess() {
        updateAll();
    }

    private void updateAll() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

}
