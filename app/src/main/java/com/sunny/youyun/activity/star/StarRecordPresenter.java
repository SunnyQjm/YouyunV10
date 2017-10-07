package com.sunny.youyun.activity.star;

import java.io.IOException;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

class StarRecordPresenter extends StarRecordContract.Presenter{

    StarRecordPresenter(StarRecordActivity starRecordActivity) {
        mView = starRecordActivity;
        mModel = new StarRecordModel(this);
    }

    @Override
    protected void start() throws IOException {

    }
}
