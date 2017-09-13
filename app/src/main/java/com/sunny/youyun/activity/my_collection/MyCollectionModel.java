package com.sunny.youyun.activity.my_collection;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

class MyCollectionModel implements MyCollectionContract.Model{
    private MyCollectionPresenter mPresenter;
    MyCollectionModel(MyCollectionPresenter myCollectionPresenter) {
        mPresenter = myCollectionPresenter;
    }
}
