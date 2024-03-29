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
        void getDataSuccess();
    }

    interface Model extends BaseModel {
        List<InternetFile> getDatas();
        void getForumDataHot(int page, boolean isRefresh);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract List<InternetFile> getDatas();
        abstract void getForumDataHot(int page, boolean isRefresh);
        abstract void getDataSuccess();
    }
}
