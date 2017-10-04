package com.sunny.youyun.activity.chat;

import java.io.IOException;

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
}
