package com.sunny.youyun.activity.file_manager.item;

import com.sunny.youyun.activity.file_manager.config.ItemTypeConfig;
import com.sunny.youyun.activity.file_manager.model.BaseLocalFileInfo;
import com.sunny.youyun.base.entity.MultiItemEntity;

/**
 * Created by Sunny on 2017/8/5 0005.
 */

public class DirectItem extends BaseLocalFileInfo implements MultiItemEntity{

    public DirectItem(Builder builder) {
        super(builder);
    }

    @Override
    public int getItemType() {
        return ItemTypeConfig.TYPE_DIRECT_INFO;
    }
}
