package com.sunny.youyun.activity.person_file_manager;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

interface PersonFileManagerContract {
    interface View extends BaseView {

    }

    interface Model extends BaseModel {

    }

    abstract class Presenter extends BasePresenter<View, Model> {

    }
}
