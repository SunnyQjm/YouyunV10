package com.sunny.youyun.activity.star;

import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.data_item.StarRecord;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

class StarRecordPresenter extends StarRecordContract.Presenter{

    StarRecordPresenter(StarRecordActivity starRecordActivity) {
        mView = starRecordActivity;
        mModel = new StarRecordModel(this);
    }

    @Override
    protected void start() throws IOException {

    }

    @Override
    void getStarRecord(int page, boolean isRefresh) {
        mModel.getStarRecord(page, ApiInfo.GET_DEFAULT_SIZE, isRefresh);
    }

    @Override
    void getStarRecordSuccess() {
        mView.getStarRecordSuccess();
    }

    @Override
    public List<StarRecord> getDatas() {
        return mModel.getDatas();
    }
}
