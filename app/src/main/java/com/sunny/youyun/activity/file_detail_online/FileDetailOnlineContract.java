package com.sunny.youyun.activity.file_detail_online;

import com.sunny.youyun.model.data_item.Comment;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/8/19 0019.
 */

interface FileDetailOnlineContract {
    interface View extends BaseView {
        void commentSuccess();
        void getCommentsSuccess(boolean isFirst);
        void getFileInfoSuccess(InternetFile internetFile);
        void starSuccess();
    }

    interface Model extends BaseModel {
        void addComment(int fileId, String content);
        void getComments(int fileId, boolean isFirst);
        void getFileInfo(String code);
        void star(int fileId);
        List<Comment> getCommentList();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void addComment(int fileId, String content);
        abstract void getComments(int fileId, boolean isFirst);
        abstract void commentsSuccess();
        abstract void getFileInfo(String code);
        abstract void getFileInfoSuccess(InternetFile internetFile);
        abstract void getCommentSuccess(boolean isFirst);
        abstract List<Comment> getCommentList();
        abstract void star(int fileId);
        abstract void starSuccess();
    }
}
