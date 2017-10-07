package com.sunny.youyun.activity.chat;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.chat.adapter.ChatAdapter;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.model.User;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.easy_refresh.ArrowPullLoadHeader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends BaseRecyclerViewActivity<ChatPresenter> implements ChatContract.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.btn_send)
    Button btnSend;

    private ChatAdapter adapter = null;
    private int page = 1;
    private User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        etContent.setHint(getString(R.string.add_message));
        refreshLayout.setHeader(new ArrowPullLoadHeader(R.layout.easy_refresh_pull_load_header));
        refreshLayout.setLoadAble(false);
        adapter = new ChatAdapter(mPresenter.getData());
        adapter.bindToRecyclerView(recyclerView);
    }

    @Override
    protected void onRefreshBegin() {
    }

    @Override
    protected void OnRefreshBeginSync() {
        page++;
        mPresenter.getMessages(userInfo.getId(), page, false);
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
    protected ChatPresenter onCreatePresenter() {
        return new ChatPresenter(this);
    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
    }

    @Override
    public void getMessagesSuccess() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void sendMessageSuccess(String content) {
        //TODO send message success
    }
}
