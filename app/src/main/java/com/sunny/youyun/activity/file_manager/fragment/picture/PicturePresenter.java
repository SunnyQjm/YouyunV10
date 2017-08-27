package com.sunny.youyun.activity.file_manager.fragment.picture;

import android.content.Context;

import com.sunny.youyun.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

class PicturePresenter extends PictureContract.Presenter{
    PicturePresenter(PictureFragment pictureFragment) {
        mView = pictureFragment;
        mModel = new PictureModel(this);
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
    void refreshData(Context context) {
        mModel.refreshData(context);
    }
}
