package com.sunny.youyun.activity.file_detail_online;

/**
 * Created by Sunny on 2017/8/19 0019.
 */

class FileDetailOnlineModel implements FileDetailOnlineContract.Model {
    private FileDetailOnlinePresenter mPresenter;

    FileDetailOnlineModel(FileDetailOnlinePresenter fileDetailOnlinePresenter) {
        mPresenter = fileDetailOnlinePresenter;
    }
}
