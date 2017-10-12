package com.sunny.youyun.activity.file_manager.fragment.picture;

import android.os.Bundle;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.base.BaseFileManagerFragment;
import com.sunny.youyun.base.entity.MultiItemEntity;

import java.util.List;

public class PictureFragment extends BaseFileManagerFragment<PicturePresenter> implements PictureContract.View {


    public PictureFragment(){

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void loadData() {
        if(isFirst && isVisible && isPrepared){
            refreshData();
            isFirst = false;
        }
    }

    public static PictureFragment newInstance() {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_RES, R.layout.fragment_picture);
        PictureFragment fragment = new PictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void refreshData() {
        mPresenter.refreshData(activity);
    }

    @Override
    protected List<MultiItemEntity> getData() {
        return mPresenter.getData();
    }

    @Override
    protected PicturePresenter onCreatePresenter() {
        return new PicturePresenter(this);
    }

    @Override
    public void allDataLoadFinish() {

    }
}

