package com.sunny.youyun.fragment.main.message_fragment;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public interface MessageContract {
    interface View extends BaseView {
        void getPrivateLetterListSuccess();
    }

    interface Model extends BaseModel {
        List<MultiItemEntity> getData();
        void getPrivateLetterList(long times, int size, boolean isRefresh);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract List<MultiItemEntity> getData();
        abstract void getPrivateLetterList(long times, boolean isRefresh);
        abstract void getPrivateLetterListSuccess();
    }
}
