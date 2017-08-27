package com.sunny.youyun.activity.file_manager.item;

import com.sunny.youyun.activity.file_manager.config.ItemTypeConfig;
import com.sunny.youyun.activity.file_manager.model.AppInfo;
import com.sunny.youyun.base.entity.MultiItemEntity;

/**
 * Created by Sunny on 2017/8/5 0005.
 */

public class AppInfoItem extends AppInfo implements MultiItemEntity{
    public AppInfoItem(Builder builder) {
        super(builder);
    }

    @Override
    public int getItemType() {
        return ItemTypeConfig.TYPE_APPLICATION_INFO;
    }
}
