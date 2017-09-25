package com.sunny.youyun.fragment.main.message_fragment;

import com.sunny.youyun.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

class MessageModel implements MessageContract.Model{
    private final MessagePresenter mPresenter;
    private final List<MultiItemEntity> mList = new ArrayList<>();
    MessageModel(MessagePresenter messagePresenter) {
        mPresenter = messagePresenter;
    }

    @Override
    public List<MultiItemEntity> getData() {
        return mList;
    }
}
