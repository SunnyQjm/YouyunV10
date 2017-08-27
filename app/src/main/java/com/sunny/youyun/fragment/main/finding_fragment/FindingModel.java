package com.sunny.youyun.fragment.main.finding_fragment;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class FindingModel implements FindingContract.Model{
    private FindingPresenter mPresenter;
    public FindingModel(FindingPresenter findingPresenter) {
        mPresenter = findingPresenter;

    }
}
