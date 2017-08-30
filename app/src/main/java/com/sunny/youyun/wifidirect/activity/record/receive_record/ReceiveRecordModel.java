package com.sunny.youyun.wifidirect.activity.record.receive_record;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

class ReceiveRecordModel implements ReceiveRecordContract.Model {
    private ReceiveRecordPresenter mPresenter;

    ReceiveRecordModel(ReceiveRecordPresenter uploadRecordPresenter) {
        mPresenter = uploadRecordPresenter;
    }

}
