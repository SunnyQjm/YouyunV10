package com.sunny.youyun.activity.person_file_manager.item;

import android.support.annotation.DrawableRes;

import com.sunny.youyun.activity.person_file_manager.config.ItemTypeConfig;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.FileTypeUtil;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

public class FileItem extends InternetFile implements MultiItemEntity {
    @DrawableRes
    private int resId = -1;

    public FileItem(Builder builder) {
        super(builder);
        resId = FileTypeUtil.getIconByFileNameWithoutVideoPhoto(getName());
    }

    public int getResId() {
        return resId;
    }

    @Override
    public int getItemType() {
        return ItemTypeConfig.TYPE_FILE_INFO;
    }
}
