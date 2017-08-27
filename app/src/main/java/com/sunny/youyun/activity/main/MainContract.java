package com.sunny.youyun.activity.main;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/24 0024.
 */

public interface MainContract {
    interface View extends BaseView {
        void changeBottomImg(int... res);
    }

    interface Model extends BaseModel {

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void changeBottomImg(int position);
    }
}
