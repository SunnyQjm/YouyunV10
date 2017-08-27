package com.sunny.youyun.activity.file_manager.fragment.application;

import android.app.Application;

import com.sunny.youyun.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

class ApplicationPresenter extends ApplicationContract.Presenter{
    ApplicationPresenter(ApplicationFragment applicationFragment) {
        mView = applicationFragment;
        mModel = new ApplicationModel(this);
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
    void refreshData(Application application) {
        mModel.refreshData(application);
    }
}
