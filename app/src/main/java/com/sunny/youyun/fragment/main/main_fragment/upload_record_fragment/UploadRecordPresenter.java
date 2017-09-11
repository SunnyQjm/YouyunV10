package com.sunny.youyun.fragment.main.main_fragment.upload_record_fragment;

import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class UploadRecordPresenter extends UploadRecordContract.Presenter{
    public UploadRecordPresenter(BaseView baseView) {
        mView = (UploadRecordContract.View) baseView;
        mModel = new UploadRecordModel(this);
    }

    @Override
    protected void start() {

    }

}
