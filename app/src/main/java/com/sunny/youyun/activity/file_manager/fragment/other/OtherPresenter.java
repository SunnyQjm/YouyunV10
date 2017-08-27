package com.sunny.youyun.activity.file_manager.fragment.other;

import com.sunny.youyun.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

class OtherPresenter extends OtherContract.Presenter{
    OtherPresenter(OtherFragment otherFragment) {
        mView = otherFragment;
        mModel = new OtherModel(this);
    }

    @Override
    protected void start() {

    }

    @Override
    void updateUI() {
        mView.updateUI();
    }

    @Override
    List<MultiItemEntity> getData() {
        return mModel.getData();
    }

    @Override
    boolean isRootPath() {
        return mModel.isRootPath();
    }

    @Override
    void back() {
        mModel.back();
    }

    @Override
    void show(int position) {
        mModel.show(position);
    }
}
