package com.sunny.youyun.views.popupwindow.search.item;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.finding_fragment.config.SearchItemType;

/**
 * Created by Sunny on 2017/10/4 0004.
 */

public class FileTag implements MultiItemEntity{
    @Override
    public int getItemType() {
        return SearchItemType.TYPE_FILE_TAG;
    }
}
