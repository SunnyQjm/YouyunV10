package com.sunny.youyun.wifidirect.activity.single_trans.trans;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;
import com.sunny.youyun.wifidirect.model.TransLocalFile;

import java.util.List;

/**
 * Created by Sunny on 2017/8/11 0011.
 */

interface TransContract {
    interface View extends BaseView {
        void updateItem(int position, TransLocalFile transLocalFile);
        void update();
    }

    interface Model extends BaseModel {
        void begin();

        void exit();

        void send(String[] paths);

        List<TransLocalFile> getData();

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void exit();

        abstract List<TransLocalFile> getData();


        abstract void send(String[] paths);
        abstract void update();
        abstract void updateItem(int i, TransLocalFile transLocalFile);
    }
}
