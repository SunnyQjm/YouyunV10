package com.sunny.youyun.activity.decim;

import android.content.Context;

import com.sunny.youyun.activity.file_manager.item.FileItem;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

class DcimPresenter extends DcimContract.Presenter{
    DcimPresenter(DcimFragment dcimFragment) {
        mView = dcimFragment;
        mModel = new DcimModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    List<FileItem> getFileItems() {
        return mModel.getFileItems();
    }

    @Override
    public void getData(Context context) {
        mModel.getData(context);
    }

    @Override
    void getDataSuccess(boolean isFirst) {
        mView.getDataSuccess(isFirst);
    }

    @Override
    List<String> getSelectItems() {
        return mModel.getSelectItems();
    }

    @Override
    void selected(int position) {
        mModel.selected(position);
    }
}
