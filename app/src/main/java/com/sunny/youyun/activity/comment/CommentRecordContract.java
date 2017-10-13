package com.sunny.youyun.activity.comment;

import com.sunny.youyun.model.data_item.CommentRecord;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

interface CommentRecordContract {
    interface View extends BaseView {
        void getCommentRecordSuccess();
    }

    interface Model extends BaseModel {
        List<CommentRecord> getDatas();
        void getCommentRecord(int page, int size, boolean isRefresh);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void getCommentRecordSuccess();
        abstract List<CommentRecord> getDatas();
        abstract void getCommentRecord(int page, boolean isRefresh);
    }
}
