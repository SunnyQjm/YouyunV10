package com.sunny.youyun.activity.chat;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.ApiInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/10/4 0004.
 */

class ChatPresenter extends ChatContract.Presenter{
    ChatPresenter(ChatActivity chatActivity) {
        mView = chatActivity;
        mModel = new ChatModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    void getMessages(int userId, int page, boolean isRefresh) {
        mModel.getMessages(userId, page, ApiInfo.GET_FORUM_DEFAULT_SIZE, isRefresh);
    }

    @Override
    void getMessagesSuccess() {
        mView.getMessagesSuccess();
    }

    @Override
    List<MultiItemEntity> getData() {
        return mModel.getData();
    }

    @Override
    void sendMessage(int userId, String content) {
        mModel.sendMessage(userId, content);
    }

    @Override
    void sendMessageSuccess(String content) {
        mView.sendMessageSuccess(content);
    }
}
