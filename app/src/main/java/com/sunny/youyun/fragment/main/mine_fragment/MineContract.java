package com.sunny.youyun.fragment.main.mine_fragment;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public interface MineContract {
    interface View extends BaseView {

    }

    interface Model extends BaseModel {

    }

    abstract class Presenter extends BasePresenter<View, Model> {

    }
}
