package com.sunny.youyun.activity.chat;

/**
 * Created by Sunny on 2017/10/4 0004.
 */

class ChatModel implements ChatContract.Model{
    private final ChatPresenter mPresenter;
    ChatModel(ChatPresenter chatPresenter) {
        mPresenter = chatPresenter;
    }
}
