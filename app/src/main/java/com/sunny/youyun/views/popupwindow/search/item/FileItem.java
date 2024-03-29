package com.sunny.youyun.views.popupwindow.search.item;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.finding_fragment.config.SearchItemType;
import com.sunny.youyun.model.InternetFile;

/**
 * Created by Sunny on 2017/10/4 0004.
 */

public class FileItem extends InternetFile implements MultiItemEntity{
    protected FileItem(Builder builder) {
        super(builder);
    }

    @Override
    public int getItemType() {
        return SearchItemType.TYPE_FILE;
    }
}
