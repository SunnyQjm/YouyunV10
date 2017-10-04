package com.sunny.youyun.activity.chat;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.views.EasyBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends MVPBaseActivity<ChatPresenter> implements ChatContract.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.btn_send)
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
    }

    @Override
    protected ChatPresenter onCreatePresenter() {
        return new ChatPresenter(this);
    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
    }
}
