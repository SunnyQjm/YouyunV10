package com.sunny.youyun.fragment.main.message_fragment;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

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

    @Override
    List<MultiItemEntity> getData() {
        return mModel.getData();
    }

    @Override
    void getPrivateLetterList(int page, boolean isRefresh) {
        mModel.getPrivateLetterList(page, ApiInfo.GET_FORUM_DEFAULT_SIZE, isRefresh);
    }

    @Override
    void getPrivateLetterListSuccess() {
        mView.getPrivateLetterListSuccess();
    }
}
