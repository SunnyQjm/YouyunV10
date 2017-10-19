package com.sunny.youyun.activity.chat;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.chat.adapter.ChatAdapter;
import com.sunny.youyun.activity.chat.config.ChatConfig;
import com.sunny.youyun.activity.chat.item.DateItem;
import com.sunny.youyun.activity.chat.item.MessageItemMy;
import com.sunny.youyun.activity.chat.item.MessageItemOther;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivityLazy;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.message_fragment.item.PrivateLetterItem;
import com.sunny.youyun.model.User;
import com.sunny.youyun.model.data_item.Message;
import com.sunny.youyun.model.event.JPushEvent;
import com.sunny.youyun.model.manager.MessageManager;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.utils.MyNotifyUtil;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.bus.MessageEventBus;
import com.sunny.youyun.utils.bus.ObjectPool;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.easy_refresh.ArrowPullLoadHeader;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Router(value = {IntentRouter.ChatActivity + "/:" + ChatConfig.PARAM_UUID},
        intParams = {ChatConfig.PARAM_USER_ID})
public class ChatActivity extends BaseRecyclerViewActivityLazy<ChatPresenter>
        implements ChatContract.View, View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    EditText etContent;
    Button btnSend;

    private User user;

    @Override
    protected void onStart() {
        super.onStart();
        MyNotifyUtil.setShowTag(MyNotifyUtil.SHOW_TAG_CHATTING);
        MessageEventBus.getInstance()
                .register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MessageEventBus.getInstance()
                .unregister(this);
    }

    @Override
    protected void onPause() {
        MyNotifyUtil.setShowTag(MyNotifyUtil.SHOW_TAG_OTHER);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
    }

    @Override
    protected void loadData(boolean isRefresh) {
        mPresenter.getMessages(user.getId(),
                adapter.getData().size() == 0 ? System.currentTimeMillis() :
                        ((Message) adapter.getItem(adapter.getData().size() - 1)).getUpdateTime()
                , isRefresh);
    }

    private void init() {
        String uuid = getIntent().getStringExtra(ChatConfig.PARAM_UUID);
        if (!uuid.equals("")) {
            user = ObjectPool.getInstance()
                    .get(uuid, User.empty());
        }

        MyNotifyUtil.setChattingId(user.getId());
        MessageManager.getInstance().clearCount(user.getId());

        etContent = (EditText) findViewById(R.id.et_content);
        btnSend = (Button) findViewById(R.id.btn_send);
        easyBar = (EasyBar) findViewById(R.id.easyBar);
        btnSend.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        refreshLayout = (EasyRefreshLayout) findViewById(R.id.refreshLayout);
        initView();
        //倒叙显示
        linerLayoutManager.setReverseLayout(true);
        easyBar.setTitle(user.getUsername());

        etContent.setHint(getString(R.string.add_message));
        refreshLayout.setHeader(new ArrowPullLoadHeader(R.layout.easy_refresh_pull_load_header));
        refreshLayout.setLoadAble(false);
        adapter = new ChatAdapter(mPresenter.getData());
        adapter.bindToRecyclerView(recyclerView);
        page = 1;
        adapter.setOnItemClickListener(this);
        loadData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(JPushEvent jPushEvent) {
        //如果收到对方的聊天信息
        if (jPushEvent.getFromUser() != null && jPushEvent.getFromUser().getId() == user.getId() &&
                jPushEvent.getType().equals(JPushEvent.INSTANTCONTACT)) {
            //TODO send message success
            adapter.addData(0, new MessageItemOther(new Message.Builder()
                    .content(jPushEvent.getContent())
                    .fromUserId(jPushEvent.getFromUser().getId())
                    .toUserId(user.getId())
                    .createTime(System.currentTimeMillis())
                    .user(jPushEvent.getFromUser())
                    .build()));
            recyclerView.scrollToPosition(0);
        }
    }

    @Override
    protected ChatPresenter onCreatePresenter() {
        return new ChatPresenter(this);
    }

    @Override
    protected void OnRefreshBeginSync() {
        page++;
        loadData(false);
    }

    @Override
    public void allDataLoadFinish() {
        refreshLayout.setRefreshAble(false);
        getMessagesSuccess();
    }

    @Override
    protected void onRefreshBegin() {
    }

    @Override
    public void getMessagesSuccess() {
        System.out.println("getMessageSuccess");
        updateAll();
    }

    @Override
    public void sendMessageSuccess(String content) {
        //TODO send message success
        Message message = (Message) adapter.getItem(0);
        long currentTime = System.currentTimeMillis();
        if (message != null && currentTime - message.getCreateTime() > 1000 * 60 * 5) {
            adapter.addData(0, new DateItem.Builder()
                    .date(currentTime)
                    .build());
        }
        adapter.addData(0, new MessageItemMy(new Message.Builder()
                .content(content)
                .fromUserId(UserInfoManager.getInstance().getUserId())
                .toUserId(user.getId())
                .createTime(currentTime)
                .user(UserInfoManager.getInstance().getUserInfo())
                .build()));
        recyclerView.scrollToPosition(0);
        MessageManager.getInstance()
                .put(user.getId(), new PrivateLetterItem(new Message.Builder()
                        .content(content)
                        .ownerId(UserInfoManager.getInstance().getUserId())
                        .targetId(user.getId())
                        .fromUserId(UserInfoManager.getInstance().getUserId())
                        .toUserId(user.getId())
                        .createTime(System.currentTimeMillis())
                        .updateTime(System.currentTimeMillis())
                        .user(user)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //发送一条消息
            case R.id.btn_send:
                mPresenter.sendMessage(user.getId(), etContent.getText().toString());
                etContent.setText("");
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MultiItemEntity multiItemEntity = (MultiItemEntity) adapter.getItem(position);
        if (multiItemEntity == null || !(multiItemEntity instanceof Message))
            return;
        Message message = (Message) multiItemEntity;
        switch (multiItemEntity.getItemType()) {
            case ChatConfig.MESSAGE_ITEM_TYPE_SELF:
            case ChatConfig.MESSAGE_ITEM_TYPE_OTHER:
                RouterUtils.openToUser(this, message.getFromUserId());
                break;

        }
    }
}
