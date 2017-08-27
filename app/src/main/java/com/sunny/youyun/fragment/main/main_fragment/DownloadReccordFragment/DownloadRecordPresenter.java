package com.sunny.youyun.fragment.main.main_fragment.DownloadReccordFragment;

import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class DownloadRecordPresenter extends DownloadRecordContract.Presenter{
    public DownloadRecordPresenter(BaseView b) {
        super();
        mView = (DownloadRecordContract.View) b;
        mModel = new DownloadRecordModel(this);
    }

    @Override
    protected void start() {

    }

    @Override
    void beginListen() {
        mModel.beginListen();
    }
}
