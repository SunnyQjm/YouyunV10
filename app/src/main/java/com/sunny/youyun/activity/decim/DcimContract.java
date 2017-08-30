package com.sunny.youyun.activity.decim;

import android.content.Context;

import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

interface DcimContract {
    interface View extends BaseView {
        void getDataSuccess(boolean isFirst);
    }

    interface Model extends BaseModel {
        List<FileItem> getFileItems();
        List<String> getSelectItems();
        void getData(Context context);
        void selected(int position);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract List<FileItem> getFileItems();
        abstract void getData(Context context);
        abstract void getDataSuccess(boolean isFirst);
        abstract List<String> getSelectItems();
        abstract void selected(int position);
    }
}
