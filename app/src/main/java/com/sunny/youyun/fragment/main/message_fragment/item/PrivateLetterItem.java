package com.sunny.youyun.fragment.main.message_fragment.item;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.message_fragment.adapter.TypeConfig;
import com.sunny.youyun.model.data_item.PrivateLetter;

/**
 * Created by Sunny on 2017/9/25 0025.
 */

public class PrivateLetterItem extends PrivateLetter implements MultiItemEntity {
    public PrivateLetterItem(Builder builder) {
        super(builder);
    }

    @Override
    public int getItemType() {
        return TypeConfig.TYPE_MESSAGE;
    }
}
