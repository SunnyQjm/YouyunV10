package com.sunny.youyun.activity.file_detail_online;

import com.sunny.youyun.model.Comment;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.YouyunAPI;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/8/19 0019.
 */

class FileDetailOnlinePresenter extends FileDetailOnlineContract.Presenter{
    FileDetailOnlinePresenter(FileDetailOnlineActivity fileDetailOnlineActivity) {
        mView = fileDetailOnlineActivity;
        mModel = new FileDetailOnlineModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    void addComment(int fileId, String content) {
        if(YouyunAPI.isIsLogin()){
            mModel.addComment(fileId, content);
        } else {
            mView.showTip("请先登陆");

        }
    }

    @Override
    void getComments(int fileId, boolean isFirst) {
        mModel.getComments(fileId, isFirst);
    }

    @Override
    void commentsSuccess() {
        mView.commentSuccess();
    }

    @Override
    void getFileInfo(String code) {
        mModel.getFileInfo(code);
    }

    @Override
    void getFileInfoSuccess(InternetFile internetFile) {
        mView.getFileInfoSuccess(internetFile);
    }

    @Override
    void getCommentSuccess(boolean isFirst) {
        mView.getCommentsSuccess(isFirst);
    }

    @Override
    List<Comment> getCommentList() {
        return mModel.getCommentList();
    }

    @Override
    void star(int fileId) {
        mModel.star(fileId);
    }

    @Override
    void starSuccess() {
        mView.starSuccess();
    }
}
