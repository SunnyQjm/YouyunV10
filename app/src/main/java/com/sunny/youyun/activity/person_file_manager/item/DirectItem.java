package com.sunny.youyun.activity.person_file_manager.item;

import com.sunny.youyun.activity.person_file_manager.config.ItemTypeConfig;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.model.InternetFile;

/**
 * Created by Sunny on 2017/8/5 0005.
 */

public class DirectItem extends InternetFile implements MultiItemEntity{

    public DirectItem(Builder builder) {
        super(builder);
    }

    @Override
    public int getItemType() {
        return ItemTypeConfig.TYPE_DIRECT_INFO;
    }
}
