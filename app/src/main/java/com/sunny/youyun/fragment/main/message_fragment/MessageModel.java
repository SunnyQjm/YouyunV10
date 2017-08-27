package com.sunny.youyun.fragment.main.message_fragment;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class MessageModel implements MessageContract.Model{
    private MessagePresenter mPresenter;
    public MessageModel(MessagePresenter messagePresenter) {
        mPresenter = messagePresenter;

    }
}
