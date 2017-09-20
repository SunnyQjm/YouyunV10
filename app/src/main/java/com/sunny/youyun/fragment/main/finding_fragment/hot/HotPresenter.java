package com.sunny.youyun.fragment.main.finding_fragment.hot;

import com.sunny.youyun.model.InternetFile;

import java.io.IOException;
import java.util.List;

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

    @Override
    List<InternetFile> getDatas() {
        return mModel.getDatas();
    }
}
