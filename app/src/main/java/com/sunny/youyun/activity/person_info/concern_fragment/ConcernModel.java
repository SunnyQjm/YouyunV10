package com.sunny.youyun.activity.person_info.concern_fragment;

import com.sunny.youyun.utils.FileTransProgressManager;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

class ConcernModel implements ConcernContract.Model{
    private ConcernPresenter mPresenter;
    private FileTransProgressManager.OnDownloadListener listener;
    ConcernModel(ConcernPresenter downloadRecordPresenter) {
        mPresenter = downloadRecordPresenter;
    }

    @Override
    public void beginListen() {

    }

}
