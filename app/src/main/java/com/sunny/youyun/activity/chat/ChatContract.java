package com.sunny.youyun.activity.chat;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/10/4 0004.
 */

interface ChatContract {
    interface View extends BaseView {
        void getMessagesSuccess();
        void sendMessageSuccess(String content);
    }

    interface Model extends BaseModel {
        void getMessages(int userId, int page, int size, boolean isRefresh);
        List<MultiItemEntity> getData();
        void sendMessage(int userId, String content);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void getMessages(int userId, int page, boolean isRefresh);
        abstract void getMessagesSuccess();
        abstract List<MultiItemEntity> getData();
        abstract void sendMessage(int userId, String content);
        abstract void sendMessageSuccess(String content);
    }
}
