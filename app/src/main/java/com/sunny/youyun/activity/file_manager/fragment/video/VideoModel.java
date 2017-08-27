package com.sunny.youyun.activity.file_manager.fragment.video;

import android.content.Context;
import android.os.Handler;

import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.activity.file_manager.item.MenuItem;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.utils.LocalDataGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

class VideoModel implements VideoContract.Model{
    private VideoPresenter mPresenter;
    private volatile List<MultiItemEntity> list = new ArrayList<>();
    private final Handler mHandler = new Handler();

    VideoModel(VideoPresenter videoPresenter) {
        this.mPresenter = videoPresenter;
    }

    @Override
    public List<MultiItemEntity> getData() {
        return list;
    }

    @Override
    public void refreshData(Context context) {
        list.clear();
        LocalDataGetter.getVideoSync(context, new LocalDataGetter.GetDataCallback<Map<String, List<FileItem>>>() {
            @Override
            public void onSuccess(Map<String, List<FileItem>> stringListMap) {
                for (Map.Entry<String, List<FileItem>> entry:
                        stringListMap.entrySet()) {
                    list.add(new MenuItem<>(entry.getKey(), entry.getValue()));
                }
                mHandler.post(()-> mPresenter.updateUI());
            }

            @Override
            public void onFail(String msg) {

            }
        });

    }
}
