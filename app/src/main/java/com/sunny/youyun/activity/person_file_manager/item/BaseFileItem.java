package com.sunny.youyun.activity.person_file_manager.item;

import com.sunny.youyun.activity.person_file_manager.config.ItemTypeConfig;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.model.InternetFile;

/**
 * Created by Sunny on 2017/9/27 0027.
 */

public class BaseFileItem extends InternetFile implements MultiItemEntity{

    protected BaseFileItem(Builder builder) {
        super(builder);
    }

    @Override
    public int getItemType() {
        return ItemTypeConfig.TYPE_BASE_FILE_INFO;
    }
}
