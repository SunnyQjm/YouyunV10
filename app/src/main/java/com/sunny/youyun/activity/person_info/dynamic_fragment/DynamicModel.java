package com.sunny.youyun.activity.person_info.dynamic_fragment;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

class DynamicModel implements DynamicContract.Model {
    private DynamicPresenter mPresenter;

    DynamicModel(DynamicPresenter uploadRecordPresenter) {
        mPresenter = uploadRecordPresenter;
    }

}
