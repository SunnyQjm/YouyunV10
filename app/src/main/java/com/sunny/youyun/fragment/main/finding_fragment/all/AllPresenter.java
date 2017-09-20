package com.sunny.youyun.fragment.main.finding_fragment.all;

import com.sunny.youyun.model.InternetFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

class AllPresenter extends AllContract.Presenter{
    AllPresenter(AllFragment allFragment) {
        mView = allFragment;
        mModel = new AllModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    void getForumDataALL(int page, boolean isRefresh) {
        mModel.getForumDataALL(page, isRefresh);
    }

    @Override
    List<InternetFile> getDatas() {
        return mModel.getDatas();
    }

    @Override
    void getForumDataSuccess() {
        mView.getForumDataSuccess();
    }

    @Override
    void allDataLoadFinish() {
        mView.allDataLoadFinish();
    }
}
