package com.sunny.youyun.activity.upload_setting;

import android.os.Handler;

import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.activity.file_manager.item.MenuItem;
import com.sunny.youyun.base.entity.MultiItemEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sunny on 2017/8/16 0016.
 */

class UploadSettingModel implements UploadSettingContract.Model {
    private final UploadSettingPresenter mPresenter;
    private final List<MultiItemEntity> mList = new ArrayList<>();
    private final Handler handler = new Handler();
    private final List<String> dataList = new ArrayList<>();

    UploadSettingModel(UploadSettingPresenter uploadSettingPresenter) {
        mPresenter = uploadSettingPresenter;
    }

    @Override
    public List<MultiItemEntity> getData(String[] paths) {
        Collections.addAll(dataList, paths);
        List<FileItem> fileItems = null;
        for (String path : paths) {
            if (fileItems == null)
                fileItems = new ArrayList<>();
            File f = new File(path);
            if (!f.exists())
                continue;
            fileItems.add(new FileItem(new FileItem.Builder()
                    .isDirect(false)
                    .lastModifiedTime(f.lastModified())
                    .size(f.length())
                    .name(f.getName())
                    .path(path)));
        }
        if (fileItems != null)
            mList.add(new MenuItem<>("已选择", fileItems));
        //回到主线程更新UI
        handler.post(mPresenter::updateUI);
        return mList;
    }

    @Override
    public void addData(String[] paths) {
        if (mList.size() == 0)
            mList.add(new MenuItem<>("已选择", null));
        if(!(mList.get(0) instanceof MenuItem)){
            return;
        }
        MenuItem<FileItem> menuItem = (MenuItem<FileItem>) mList.get(0);
        for (String path : paths) {
            if(dataList.contains(path))
                continue;
            File f = new File(path);
            if (!f.exists())
                continue;
            menuItem.addSubItem(new FileItem(new FileItem.Builder()
                    .isDirect(false)
                    .lastModifiedTime(f.lastModified())
                    .size(f.length())
                    .name(f.getName())
                    .path(path)));
        }
        handler.post(mPresenter::updateUI);
    }
}
