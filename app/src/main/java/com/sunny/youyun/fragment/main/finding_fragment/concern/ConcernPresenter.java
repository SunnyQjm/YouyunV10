package com.sunny.youyun.fragment.main.finding_fragment.concern;

import com.sunny.youyun.model.InternetFile;

import java.io.IOException;
import java.util.List;

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

    @Override
    List<InternetFile> getDatas() {
        return mModel.getDatas();
    }
}
