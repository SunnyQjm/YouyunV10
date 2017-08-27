package com.sunny.youyun.activity.file_manager.fragment.video;

import android.os.Bundle;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.base.BaseFileManagerFragment;
import com.sunny.youyun.base.entity.MultiItemEntity;

import java.util.List;

public class VideoFragment extends BaseFileManagerFragment<VideoPresenter> implements VideoContract.View {


    public VideoFragment(){

    }

    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance() {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_RES, R.layout.fragment_video);
        VideoFragment fragment = new VideoFragment();
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
    protected VideoPresenter onCreatePresenter() {
        return new VideoPresenter(this);
    }
}
