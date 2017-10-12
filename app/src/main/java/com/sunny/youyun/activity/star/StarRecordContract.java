package com.sunny.youyun.activity.star;

import com.sunny.youyun.model.data_item.StarRecord;
import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

interface StarRecordContract {
    interface View extends BaseView {
        void getStarRecordSuccess();
    }

    interface Model extends BaseModel {
        void getStarRecord(int page, int size, boolean isRefresh);
        List<StarRecord> getDatas();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void getStarRecord(int page, boolean isRefresh);
        abstract void getStarRecordSuccess();

        public abstract List<StarRecord> getDatas();
    }
}
