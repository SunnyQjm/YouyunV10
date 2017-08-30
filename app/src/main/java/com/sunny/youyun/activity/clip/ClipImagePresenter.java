package com.sunny.youyun.activity.clip;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

class ClipImagePresenter extends ClipImageContrat.Presenter{
    ClipImagePresenter(ClipImageActivity clipImageActivity) {
        mView = clipImageActivity;
        mModel = new ClipImageModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    void updateSuccess() {
        mView.updateSuccess();
    }

    @Override
    void updateFail() {
        mView.updateFail();
    }

    @Override
    void saveFile(Observable<File> saveFileTO) {
        mModel.saveFile(saveFileTO);
    }
}
