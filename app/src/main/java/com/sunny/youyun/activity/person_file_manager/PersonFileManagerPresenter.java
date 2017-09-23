package com.sunny.youyun.activity.person_file_manager;

import com.sunny.youyun.base.entity.MultiItemEntity;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

class PersonFileManagerPresenter extends PersonFileManagerContract.Presenter{
    PersonFileManagerPresenter(PersonFileManagerActivity personFileManagerActivity) {
        mView = personFileManagerActivity;
        mModel = new PersonFileManagerModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    List<MultiItemEntity> getData() {
        return mModel.getData();
    }

    @Override
    void getUploadFilesOnline(String parent) {
        mModel.getUploadFilesOnline(parent);
    }

    @Override
    void getUploadFilesSuccess() {
        mView.getUploadFilesSuccess();
    }

    @Override
    void createDirectory(String parentId, String name) {
        mModel.createDirectory(parentId, name);

    }

    @Override
    void createDirectorySuccess() {
        mView.createDirectorySuccess();
    }
}
