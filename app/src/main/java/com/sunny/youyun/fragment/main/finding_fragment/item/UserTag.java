package com.sunny.youyun.fragment.main.finding_fragment.item;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.finding_fragment.config.SearchItemType;

/**
 * Created by Sunny on 2017/10/4 0004.
 */

public class UserTag implements MultiItemEntity{

    @Override
    public int getItemType() {
        return SearchItemType.TYPE_USER_TAG;
    }
}
