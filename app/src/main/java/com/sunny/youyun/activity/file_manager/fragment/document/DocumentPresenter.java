package com.sunny.youyun.activity.file_manager.fragment.document;

import android.content.Context;

import com.sunny.youyun.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

class DocumentPresenter extends DocumentContract.Presenter{
    DocumentPresenter(DocumentFragment documentFragment) {
        mView = documentFragment;
        mModel = new DocumentModel(this);
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
