package com.sunny.youyun.fragment.main.finding_fragment.concern;

import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

interface ConcernContract {
    interface View extends BaseView {
        void getDatasOnlineSuccess();
    }

    interface Model extends BaseModel {
        List<InternetFile> getDatas();
        void getConcernPeopleShares(int page, int size, boolean isRefresh);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract List<InternetFile> getDatas();
        abstract void getConcernPeopleShares(int page, boolean isRefresh);
        abstract void getDatasOnlineSuccess();
    }
}
