package com.sunny.youyun.fragment.main.message_fragment;

import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class MessagePresenter extends MessageContract.Presenter{

    public MessagePresenter(BaseView baseView) {
        mView = (MessageContract.View) baseView;
        mModel = new MessageModel(this);
    }

    @Override
    protected void start() {

    }
}
