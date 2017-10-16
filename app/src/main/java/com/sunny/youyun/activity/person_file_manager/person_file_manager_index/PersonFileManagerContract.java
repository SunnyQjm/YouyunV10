package com.sunny.youyun.activity.person_file_manager.person_file_manager_index;

import com.sunny.youyun.activity.person_file_manager.item.PathItem;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

interface PersonFileManagerContract {
    interface View extends BaseView {
        void getFilesByPathSuccess();
        void deleteSuccess(int position);
        void getPathsSuccess();
    }

    interface Model extends BaseModel {
        List<MultiItemEntity> getData();
        void getFilesByPath(String parentId, int page, int size, boolean isRefresh);
        void delete(String id, int position);
        //path
        List<PathItem> getPaths();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract List<MultiItemEntity> getData();
        abstract void getFilesByPath(String parentId, int page, boolean isRefresh);
        abstract void getFilesByPathSuccess();
        abstract void getPathsSuccess();
        //path
        abstract List<PathItem> getPaths();
        abstract void delete(String id, int position);
        abstract void deleteSuccess(int position);
    }
}
