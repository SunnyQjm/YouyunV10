package com.sunny.youyun.fragment.main.finding_fragment.hot;

import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

interface HotContract {
    interface View extends BaseView {

    }

    interface Model extends BaseModel {
        List<InternetFile> getDatas();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract List<InternetFile> getDatas();
    }
}
