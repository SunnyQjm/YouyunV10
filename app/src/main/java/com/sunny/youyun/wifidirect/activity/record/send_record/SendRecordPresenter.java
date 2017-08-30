package com.sunny.youyun.wifidirect.activity.record.send_record;

import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class SendRecordPresenter extends SendRecordContract.Presenter{
    public SendRecordPresenter(BaseView b) {
        super();
        mView = (SendRecordContract.View) b;
        mModel = new SendRecordModel(this);
    }

    @Override
    protected void start() {

    }

    @Override
    void beginListen() {
        mModel.beginListen();
    }
}
