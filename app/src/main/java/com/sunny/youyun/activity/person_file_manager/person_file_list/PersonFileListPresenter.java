package com.sunny.youyun.activity.person_file_manager.person_file_list;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.ApiInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/9/25 0025.
 */

class PersonFileListPresenter extends PersonFileListContract.Presenter{
    PersonFileListPresenter(PersonFileListActivity personFileListActivity) {
        mView = personFileListActivity;
        mModel = new PersonFileListModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    List<MultiItemEntity> getData() {
        return mModel.getData();
    }

    @Override
    void getFileByTypeSuccess() {
        mView.getFileByTypeSuccess();
    }

    @Override
    void getFileByPathSuccess() {
        mView.getFileByPathSuccess();
    }

    @Override
    void getFileByType(String MIME, int page, boolean isRefresh) {
        mModel.getFileByType(MIME, page, ApiInfo.GET_DEFAULT_SIZE, isRefresh);
    }

    @Override
    void getFileByPath(String parentId) {
        mModel.getFileByPath(parentId);
    }
}
