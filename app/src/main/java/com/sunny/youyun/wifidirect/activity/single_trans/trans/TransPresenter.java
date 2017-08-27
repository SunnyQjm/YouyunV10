package com.sunny.youyun.wifidirect.activity.single_trans.trans;

import com.sunny.youyun.wifidirect.model.TransLocalFile;

import java.util.List;

/**
 * Created by Sunny on 2017/8/11 0011.
 */

class TransPresenter extends TransContract.Presenter{
    TransPresenter(TransActivity transFragment) {
        mView = transFragment;
        mModel = new TransModel(this);
    }

    @Override
    protected void start() {
        mModel.begin();
    }

    @Override
    void exit() {
        mModel.exit();
    }

    @Override
    List<TransLocalFile> getData() {
        return mModel.getData();
    }


    @Override
    void send(String[] paths) {
        mModel.send(paths);
    }

    @Override
    void update() {
        mView.update();
    }

    @Override
    void updateItem(int i, TransLocalFile transLocalFile) {
        mView.updateItem(i, transLocalFile);
    }
}
