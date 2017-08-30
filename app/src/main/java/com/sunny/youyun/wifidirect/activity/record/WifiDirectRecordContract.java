package com.sunny.youyun.wifidirect.activity.record;

import com.sunny.youyun.mvp.BaseModel;
import com.sunny.youyun.mvp.BasePresenter;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

interface WifiDirectRecordContract {
    interface View extends BaseView {

        }

        interface Model extends BaseModel {

        }

        abstract class Presenter extends BasePresenter<View, Model> {

        }
}
