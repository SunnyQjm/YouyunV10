package com.sunny.youyun.wifidirect.activity.record.send_record;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public interface SendRecordContract {
    interface View extends BaseView {
    }
    interface Model extends BaseModel {
        void beginListen();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        abstract void beginListen();
    }
}
