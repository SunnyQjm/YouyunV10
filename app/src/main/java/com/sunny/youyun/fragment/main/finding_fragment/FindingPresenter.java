package com.sunny.youyun.fragment.main.finding_fragment;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.mvp.BaseView;

import java.util.List;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class FindingPresenter extends FindingContract.Presenter{
    public FindingPresenter(BaseView baseView) {
        mView = (FindingContract.View) baseView;
        mModel = new FindingModel(this);
    }

    @Override
    protected void start() {

    }
}
