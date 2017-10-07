package com.sunny.youyun.activity.comment;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

class CommentRecordModel implements CommentRecordContract.Model{
    private final CommentRecordPresenter mPresenter;
    CommentRecordModel(CommentRecordPresenter commentRecordPresenter) {
        mPresenter = commentRecordPresenter;
    }
}
