package com.sunny.youyun.activity.star;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

class StarRecordModel implements StarRecordContract.Model{
    private final StarRecordPresenter mPresenter;
    StarRecordModel(StarRecordPresenter starRecordPresenter) {
        mPresenter = starRecordPresenter;
    }
}
