package com.sunny.youyun.activity.file_manager.item;

import com.sunny.youyun.activity.file_manager.config.ItemTypeConfig;
import com.sunny.youyun.base.AbstractExpandableItem;
import com.sunny.youyun.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

public class MenuItem<T extends MultiItemEntity> extends AbstractExpandableItem<T> implements MultiItemEntity{

    private final String title;
    public MenuItem(String title, List<T> list) {
        this.title = title;
        if(list == null)
            list = new ArrayList<>();
        this.mSubItems = list;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return ItemTypeConfig.TYPE_LEVEL_0;
    }
}
