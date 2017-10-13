package com.sunny.youyun.activity.person_file_manager.person_file_type;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/9/25 0025.
 */

interface PersonFileListTypeContract {
    interface View extends BaseView {
        void getFileByTypeSuccess();
    }

    interface Model extends BaseModel {
        List<MultiItemEntity> getData();
        void getFileByType(String MIME, int page, int size, boolean isRefresh);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract List<MultiItemEntity> getData();
        abstract void getFileByTypeSuccess();
        abstract void getFileByType(String MIME, int page, boolean isRefresh);
    }
}
