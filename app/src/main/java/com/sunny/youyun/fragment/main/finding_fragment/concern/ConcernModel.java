package com.sunny.youyun.fragment.main.finding_fragment.concern;

import com.sunny.youyun.model.InternetFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

class ConcernModel implements ConcernContract.Model{
    private final ConcernPresenter mPresenter;
    private final List<InternetFile> internetFiles = new ArrayList<>();
    ConcernModel(ConcernPresenter concernPresenter) {
        mPresenter = concernPresenter;
    }

    @Override
    public List<InternetFile> getDatas() {
        return internetFiles;
    }
}
