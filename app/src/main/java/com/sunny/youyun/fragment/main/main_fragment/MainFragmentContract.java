package com.sunny.youyun.fragment.main.main_fragment;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/22 0022.
 */

interface MainFragmentContract {
    interface View extends BaseView {
    }

    interface Model extends BaseModel {
    }

    abstract class Presenter extends BasePresenter<View, Model> {

    }

}
