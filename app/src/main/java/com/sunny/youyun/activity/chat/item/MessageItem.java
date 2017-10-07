package com.sunny.youyun.activity.chat.item;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.model.data_item.Message;

/**
 * Created by Sunny on 2017/10/6 0006.
 */

public class MessageItem extends Message implements MultiItemEntity {
    protected MessageItem(Builder builder) {
        super(builder);
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
