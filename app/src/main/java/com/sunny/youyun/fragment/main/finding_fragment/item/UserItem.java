package com.sunny.youyun.fragment.main.finding_fragment.item;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.finding_fragment.config.SearchItemType;
import com.sunny.youyun.model.User;

/**
 * Created by Sunny on 2017/10/4 0004.
 */

public class UserItem extends User implements MultiItemEntity{
    private UserItem(Builder builder) {
        super(builder);
    }

    @Override
    public int getItemType() {
        return SearchItemType.TYPE_USER;
    }
}
