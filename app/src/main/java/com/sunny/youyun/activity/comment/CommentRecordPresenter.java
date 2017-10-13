package com.sunny.youyun.activity.comment;

import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.data_item.CommentRecord;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

class CommentRecordPresenter extends CommentRecordContract.Presenter{
    CommentRecordPresenter(CommentRecordActivity commentRecordActivity) {
        mView = commentRecordActivity;
        mModel = new CommentRecordModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    void getCommentRecordSuccess() {
        mView.getCommentRecordSuccess();
    }

    @Override
    List<CommentRecord> getDatas() {
        return mModel.getDatas();
    }

    @Override
    void getCommentRecord(int page, boolean isRefresh) {
        mModel.getCommentRecord(page, ApiInfo.GET_DEFAULT_SIZE, isRefresh);
    }
}
