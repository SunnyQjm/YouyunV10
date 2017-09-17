package com.sunny.youyun.fragment.main.finding_fragment.all;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

interface AllContract {
    interface View extends BaseView {

    }

    interface Model extends BaseModel {
        void getForumDataALL(int page);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void getForumDataALL(int page);
    }
}
