package com.sunny.youyun.fragment.main.finding_fragment;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

class FindingModel implements FindingContract.Model {
    private final FindingPresenter mPresenter;
    FindingModel(FindingPresenter findingPresenter) {
        mPresenter = findingPresenter;
    }

}
