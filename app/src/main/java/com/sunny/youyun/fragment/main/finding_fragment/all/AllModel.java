package com.sunny.youyun.fragment.main.finding_fragment.all;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

class AllModel implements AllContract.Model{
    private final AllPresenter mPresenter;
    AllModel(AllPresenter allPresenter) {
        mPresenter = allPresenter;
    }

    @Override
    public void getForumDataALL(int page) {

    }
}
