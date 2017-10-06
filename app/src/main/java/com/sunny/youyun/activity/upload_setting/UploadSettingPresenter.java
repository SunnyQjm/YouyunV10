package com.sunny.youyun.activity.upload_setting;

import com.sunny.youyun.base.entity.MultiItemEntity;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/8/16 0016.
 */

class UploadSettingPresenter extends UploadSettingContract.Presenter{
    UploadSettingPresenter(UploadSettingActivity uploadSettingActivity) {
        mView = uploadSettingActivity;
        mModel = new UploadSettingModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    List<MultiItemEntity> getData(String[] paths) {
        return mModel.getData(paths);
    }

    @Override
    void updateUI() {
        mView.updateUI();
    }

    @Override
    String[] getPaths() {
        return mModel.getPaths();
    }

    @Override
    void remove(int position) {
        mModel.remove(position);
    }

    @Override
    void addData(String[] paths) {
        mModel.addData(paths);
    }
}
