package com.sunny.youyun.activity.my_collection;

import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.data_item.Collection;

import java.io.IOException;
import java.util.List;

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

    @Override
    void getCollections(int page, boolean isRefresh) {
        mModel.getCollections(page, ApiInfo.GET_FORUM_DEFAULT_SIZE, isRefresh);
    }

    @Override
    List<Collection> getData() {
        return mModel.getData();
    }

    @Override
    void getCollectionsSuccess() {
        mView.getCollectionsSuccess();
    }
}
