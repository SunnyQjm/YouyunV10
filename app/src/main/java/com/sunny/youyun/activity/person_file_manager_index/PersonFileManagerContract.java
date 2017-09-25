package com.sunny.youyun.activity.person_file_manager_index;

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
        void getUploadFilesSuccess();
        void createDirectorySuccess();
    }

    interface Model extends BaseModel {
        List<MultiItemEntity> getData();
        void getUploadFilesOnline(String parent);
        void createDirectory(String parentId, String name);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract List<MultiItemEntity> getData();
        abstract void getUploadFilesOnline(String parent);
        abstract void getUploadFilesSuccess();
        abstract void createDirectory(String parentId, String name);
        abstract void createDirectorySuccess();
    }
}
