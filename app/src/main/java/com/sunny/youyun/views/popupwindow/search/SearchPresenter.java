package com.sunny.youyun.views.popupwindow.search;

import com.sunny.youyun.base.entity.MultiItemEntity;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/10/19 0019.
 */

class SearchPresenter extends SearchContract.Presenter{
    SearchPresenter(SearchWindow searchWindow) {
        mView = searchWindow;
        mModel = new SearchModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    void search(String str) {
        mModel.search(str);
    }

    @Override
    void searchSuccess() {
        mView.searchSuccess();
    }

    @Override
    List<MultiItemEntity> getData() {
        return mModel.getData();
    }
}
