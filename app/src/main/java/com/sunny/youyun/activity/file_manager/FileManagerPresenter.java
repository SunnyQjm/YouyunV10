package com.sunny.youyun.activity.file_manager;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

class FileManagerPresenter extends FileManagerContract.Presenter{

    FileManagerPresenter(FileManagerContract.View view) {
        mView = view;
        mModel = new FileManagerModel(this);
    }

    @Override
    protected void start() {

    }
}
