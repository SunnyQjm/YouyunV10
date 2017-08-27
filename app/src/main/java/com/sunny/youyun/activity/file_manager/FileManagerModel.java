package com.sunny.youyun.activity.file_manager;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

class FileManagerModel implements FileManagerContract.Model{
    private FileManagerPresenter mPresenter;

    FileManagerModel(FileManagerPresenter fileManagerPresenter) {
        this.mPresenter = fileManagerPresenter;
    }
}
