package com.sunny.youyun.wifidirect.activity.record.receive_record;

import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class ReceiveRecordPresenter extends ReceiveRecordContract.Presenter{
    public ReceiveRecordPresenter(BaseView baseView) {
        mView = (ReceiveRecordContract.View) baseView;
        mModel = new ReceiveRecordModel(this);
    }

    @Override
    protected void start() {

    }

}
