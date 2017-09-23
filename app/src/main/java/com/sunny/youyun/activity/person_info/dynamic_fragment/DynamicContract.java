package com.sunny.youyun.activity.person_info.dynamic_fragment;

import com.sunny.youyun.model.Dynamic;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public interface DynamicContract {
    interface View extends BaseView {
        void getDynamicSuccess();
    }
    interface Model extends BaseModel {
        void getDynamic(int page, int size, boolean isRefresh);
        List<Dynamic> getData();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void getDynamic(int page, boolean isRefresh);
        abstract void getDynamicSuccess();
        abstract List<Dynamic> getData();
    }
}
