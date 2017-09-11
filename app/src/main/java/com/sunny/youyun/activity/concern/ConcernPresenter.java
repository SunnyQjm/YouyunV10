package com.sunny.youyun.activity.concern;

import java.io.IOException;

/**
 * Created by Sunny on 2017/9/11 0011.
 */

class ConcernPresenter extends ConcernContract.Presenter{
    ConcernPresenter(ConcernActivity concernActivity) {
        mView = concernActivity;
        mModel = new ConcernModel(this);
    }

    @Override
    protected void start() throws IOException {

    }
}
