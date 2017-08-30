package com.sunny.youyun.wifidirect.activity.record.send_record;

import com.sunny.youyun.utils.FileTransProgressManager;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

class SendRecordModel implements SendRecordContract.Model{
    private SendRecordPresenter mPresenter;
    private FileTransProgressManager.OnDownloadListener listener;
    SendRecordModel(SendRecordPresenter downloadRecordPresenter) {
        mPresenter = downloadRecordPresenter;
    }

    @Override
    public void beginListen() {

    }

}
