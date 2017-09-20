package com.sunny.youyun.fragment.main.finding_fragment.hot;

import com.sunny.youyun.model.InternetFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 2017/9/14 0014.
 */

class HotModel implements HotContract.Model{
    private final HotPresenter mPresenter;
    private final List<InternetFile> internetFiles = new ArrayList<>();
    HotModel(HotPresenter hotPresenter) {
        mPresenter = hotPresenter;
    }

    @Override
    public List<InternetFile> getDatas() {
        return internetFiles;
    }
}
