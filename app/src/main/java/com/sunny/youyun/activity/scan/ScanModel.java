package com.sunny.youyun.activity.scan;

/**
 * Created by Sunny on 2017/8/11 0011.
 */

class ScanModel implements ScanContract.Model{
    private ScanPresenter mPresenter;
    ScanModel(ScanPresenter scanPresenter) {
        mPresenter = scanPresenter;
    }
}
