package com.sunny.youyun.views.popupwindow.directory_select;

import com.sunny.youyun.activity.person_file_manager.item.PathItem;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/10/14 0014.
 */

interface DirectSelectContract {
    interface View extends BaseView {
        void getFilesByPathSuccess();
        void getPathsSuccess();
    }

    interface Model extends BaseModel {
        List<MultiItemEntity> getDatas();
        void getFilesByPath(String parentId, int page, int size, boolean isRefresh);

        //path
        List<PathItem> getPaths();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void getFilesByPath(String parentId, int page, boolean isRefresh);
        abstract List<MultiItemEntity> getDatas();
        abstract void getFilesByPathSuccess();
        abstract List<PathItem> getPaths();
        abstract void getPathsSuccess();
    }
}
