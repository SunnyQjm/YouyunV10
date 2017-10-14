package com.sunny.youyun.views.popupwindow.directory_select;

import com.sunny.youyun.activity.person_file_manager.item.PathItem;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.ApiInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/10/14 0014.
 */

class DirectSelectPresenter extends DirectSelectContract.Presenter{
    DirectSelectPresenter(DirectSelectPopupWindow activity) {
        mView = activity;
        mModel = new DirectSelectModel(this);
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
}
