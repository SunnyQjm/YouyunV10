package com.sunny.youyun.activity.person_file_manager_index.item;

import com.sunny.youyun.activity.person_file_manager_index.config.ItemTypeConfig;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.model.FileManageItem;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

public class FileItem extends FileManageItem implements MultiItemEntity {
    private FileItem(Builder builder) {
        super(builder);
    }

    @Override
    public int getItemType() {
        if (getFile() == null) {
            return ItemTypeConfig.TYPE_DIRECT_INFO;
        } else {
            return ItemTypeConfig.TYPE_FILE_INFO;
        }
    }
}
