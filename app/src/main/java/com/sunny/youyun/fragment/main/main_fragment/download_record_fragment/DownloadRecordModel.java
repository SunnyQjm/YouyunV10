package com.sunny.youyun.fragment.main.main_fragment.download_record_fragment;

import com.sunny.youyun.utils.FileTransProgressManager;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

class DownloadRecordModel implements DownloadRecordContract.Model{
    private DownloadRecordPresenter mPresenter;
    private FileTransProgressManager.OnDownloadListener listener;
    DownloadRecordModel(DownloadRecordPresenter downloadRecordPresenter) {
        mPresenter = downloadRecordPresenter;
    }

    @Override
    public void beginListen() {

    }

}
