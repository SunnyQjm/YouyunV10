package com.sunny.youyun.fragment.main.finding_fragment.all;

import java.io.IOException;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

class AllPresenter extends AllContract.Presenter{
    AllPresenter(AllFragment allFragment) {
        mView = allFragment;
        mModel = new AllModel(this);
    }

    @Override
    protected void start() throws IOException {

    }
}
