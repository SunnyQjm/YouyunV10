package com.sunny.youyun.activity.clip;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.io.File;

import io.reactivex.Observable;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

interface ClipImageContrat {
    interface View extends BaseView {
        void updateSuccess();
        void updateFail();
    }

    interface Model extends BaseModel {
        void saveFile(Observable<File> saveFileTO);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void updateSuccess();
        abstract void updateFail();
        abstract void saveFile(Observable<File> saveFileTO);
    }
}
