package com.sunny.youyun.fragment.main.main_fragment.upload_record_fragment;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

class UploadRecordModel implements UploadRecordContract.Model {
    private UploadRecordPresenter mPresenter;

    UploadRecordModel(UploadRecordPresenter uploadRecordPresenter) {
        mPresenter = uploadRecordPresenter;
    }

}
