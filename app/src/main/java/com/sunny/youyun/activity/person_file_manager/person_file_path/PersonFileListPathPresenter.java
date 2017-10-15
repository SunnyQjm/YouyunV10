package com.sunny.youyun.activity.person_file_manager.person_file_path;

import com.sunny.youyun.activity.person_file_manager.item.PathItem;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.ApiInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

class PersonFileListPathPresenter extends PersonFileListPathContract.Presenter{
    PersonFileListPathPresenter(PersonFileListPathActivity personFileListPathActivity) {
        mView = personFileListPathActivity;
        mModel = new PersonFileListPathModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    void getFilesByPath(String parentId, int page, boolean isRefresh) {
        mModel.getFilesByPath(parentId, page, ApiInfo.GET_DEFAULT_SIZE, isRefresh);
    }

    @Override
    List<MultiItemEntity> getDatas() {
        return mModel.getDatas();
    }

    @Override
    void getFilesByPathSuccess() {
        mView.getFilesByPathSuccess();
    }

    @Override
    List<PathItem> getPaths() {
        return mModel.getPaths();
    }

    @Override
    void getPathsSuccess() {
        mView.getPathsSuccess();
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
