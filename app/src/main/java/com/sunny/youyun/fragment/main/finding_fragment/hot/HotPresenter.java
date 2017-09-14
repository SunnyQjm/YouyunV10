package com.sunny.youyun.fragment.main.finding_fragment.hot;

import java.io.IOException;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

class HotPresenter extends HotContract.Presenter{
    HotPresenter(HotFragment hotFragment) {
        mView = hotFragment;
        mModel = new HotModel(this);
    }

    @Override
    protected void start() throws IOException {

    }
}
