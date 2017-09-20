package com.sunny.youyun.activity.file_manager.fragment.document;

import android.os.Bundle;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.base.BaseFileManagerFragment;
import com.sunny.youyun.base.entity.MultiItemEntity;

import java.util.List;

public class DocumentFragment extends BaseFileManagerFragment<DocumentPresenter> implements DocumentContract.View {

    public DocumentFragment(){

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

    public static DocumentFragment newInstance() {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_RES, R.layout.fragment_document);
        DocumentFragment fragment = new DocumentFragment();
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
    protected DocumentPresenter onCreatePresenter() {
        return new DocumentPresenter(this);
    }


}
