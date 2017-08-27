package com.sunny.youyun.activity.file_detail_online;

import java.io.IOException;

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
}
