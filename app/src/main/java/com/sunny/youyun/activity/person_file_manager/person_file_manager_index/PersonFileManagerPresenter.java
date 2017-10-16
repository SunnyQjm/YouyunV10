package com.sunny.youyun.activity.person_file_manager.person_file_manager_index;

import com.sunny.youyun.activity.person_file_manager.item.PathItem;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.ApiInfo;

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
    void getFilesByPath(String parentId, int page, boolean isRefresh) {
        mModel.getFilesByPath(parentId, page, ApiInfo.GET_DEFAULT_SIZE, isRefresh);
    }

    @Override
    void getFilesByPathSuccess() {
        mView.getFilesByPathSuccess();
    }

    @Override
    void getPathsSuccess() {
        mView.getPathsSuccess();
    }

    @Override
    List<PathItem> getPaths() {
        return mModel.getPaths();
    }

    @Override
    void delete(String id, int position) {
        mModel.delete(id, position);
    }

    @Override
    void deleteSuccess(int position) {
        mView.deleteSuccess(position);
    }
}
