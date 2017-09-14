package com.sunny.youyun.fragment.main.finding_fragment.concern;

import java.io.IOException;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

class ConcernPresenter extends ConcernContract.Presenter{
    ConcernPresenter(ConcernFragment concernFragment) {
        mView = concernFragment;
        mModel = new ConcernModel(this);
    }

    @Override
    protected void start() throws IOException {

    }
}
