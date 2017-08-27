package com.sunny.youyun.activity.file_manager.fragment.video;

import android.content.Context;

import com.sunny.youyun.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

class VideoPresenter extends VideoContract.Presenter{
    VideoPresenter(VideoFragment videoFragment) {
        mView = videoFragment;
        mModel = new VideoModel(this);
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
