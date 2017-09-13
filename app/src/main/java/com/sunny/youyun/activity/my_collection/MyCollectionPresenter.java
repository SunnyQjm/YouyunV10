package com.sunny.youyun.activity.my_collection;

import java.io.IOException;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

class MyCollectionPresenter extends MyCollectionContract.Presenter{
    MyCollectionPresenter(MyCollectionActivity myCollectionActivity) {
        mView = myCollectionActivity;
        mModel = new MyCollectionModel(this);
    }

    @Override
    protected void start() throws IOException {

    }
}
