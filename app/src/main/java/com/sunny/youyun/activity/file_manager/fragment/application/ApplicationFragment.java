package com.sunny.youyun.activity.file_manager.fragment.application;

import android.os.Bundle;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.base.BaseFileManagerFragment;
import com.sunny.youyun.base.entity.MultiItemEntity;

import java.util.List;

public class ApplicationFragment extends BaseFileManagerFragment<ApplicationPresenter> implements ApplicationContract.View {

    public ApplicationFragment(){

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


    public static ApplicationFragment newInstance() {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_RES, R.layout.fragment_application_fgrament);
        ApplicationFragment fragment = new ApplicationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void refreshData() {
        mPresenter.refreshData(activity.getApplication());
    }

    @Override
    protected List<MultiItemEntity> getData() {
        return mPresenter.getData();
    }

    @Override
    protected ApplicationPresenter onCreatePresenter() {
        return new ApplicationPresenter(this);
    }

}
