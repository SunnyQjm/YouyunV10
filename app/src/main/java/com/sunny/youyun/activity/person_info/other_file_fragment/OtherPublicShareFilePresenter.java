package com.sunny.youyun.activity.person_info.other_file_fragment;

import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.InternetFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/10/10 0010.
 */

class OtherPublicShareFilePresenter extends OtherPublicShareFileContract.Presenter{
    OtherPublicShareFilePresenter(OtherPublicShareFileFragment otherPublicShareFileFragment) {
        mView = otherPublicShareFileFragment;
        mModel = new OtherPublicShareFileModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    List<InternetFile> getData() {
        return mModel.getData();
    }

    @Override
    void getFiles(int userId, int page, boolean b) {
        mModel.getFiles(userId, page, ApiInfo.GET_DEFAULT_SIZE, b);
    }

    @Override
    void getOtherPublicFilesSuccess() {
        mView.getOtherPublicFilesSuccess();
    }

    @Override
    void allDataGetFinish() {
        mView.allDataLoadFinish();
    }
}
