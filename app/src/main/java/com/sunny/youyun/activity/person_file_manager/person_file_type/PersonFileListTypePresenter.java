package com.sunny.youyun.activity.person_file_manager.person_file_type;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.ApiInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/9/25 0025.
 */

class PersonFileListTypePresenter extends PersonFileListTypeContract.Presenter{
    PersonFileListTypePresenter(PersonFileListTypeActivity personFileListActivity) {
        mView = personFileListActivity;
        mModel = new PersonFileListTypeModel(this);
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
    void getFileByType(String MIME, int page, boolean isRefresh) {
        mModel.getFileByType(MIME, page, ApiInfo.GET_DEFAULT_SIZE, isRefresh);
    }
}
