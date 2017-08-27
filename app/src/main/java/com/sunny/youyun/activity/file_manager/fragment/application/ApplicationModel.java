package com.sunny.youyun.activity.file_manager.fragment.application;

import android.app.Application;
import android.os.Handler;

import com.sunny.youyun.activity.file_manager.item.AppInfoItem;
import com.sunny.youyun.activity.file_manager.item.MenuItem;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.utils.LocalDataGetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

class ApplicationModel implements ApplicationContract.Model{
    private ApplicationPresenter mPresenter;
    private final List<MultiItemEntity> list = new ArrayList<>();
    private Handler handler = new Handler();
    ApplicationModel(ApplicationPresenter applicationPresenter) {
        mPresenter = applicationPresenter;
    }

    @Override
    public List<MultiItemEntity> getData() {
        return list;
    }

    @Override
    public void refreshData(Application application) {
        LocalDataGetter.getAppInfosIgnoreSystemAppSync(application, new LocalDataGetter.GetDataCallback<List<AppInfoItem>>() {
            @Override
            public void onSuccess(List<AppInfoItem> appInfos) {
                list.clear();
                list.add(new MenuItem<>("已安装", appInfos));
                handler.post(()-> mPresenter.updateUI());
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }
}
