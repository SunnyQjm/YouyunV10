package com.sunny.youyun.activity.comment;

import java.io.IOException;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

class CommentRecordPresenter extends CommentRecordContract.Presenter{
    CommentRecordPresenter(CommentRecordActivity commentRecordActivity) {
        mView = commentRecordActivity;
        mModel = new CommentRecordModel(this);
    }

    @Override
    protected void start() throws IOException {

    }
}
