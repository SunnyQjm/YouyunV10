package com.sunny.youyun.fragment.main.finding_fragment.all;

import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

interface AllContract {
    interface View extends BaseView {
        void getForumDataSuccess();
    }

    interface Model extends BaseModel {
        void getForumDataALL(int page, boolean isRefresh);
        List<InternetFile> getDatas();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void getForumDataALL(int page, boolean isRefresh);
        abstract List<InternetFile> getDatas();
        abstract void getForumDataSuccess();
    }
}
