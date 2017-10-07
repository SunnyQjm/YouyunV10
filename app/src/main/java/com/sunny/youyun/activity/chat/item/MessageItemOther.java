package com.sunny.youyun.activity.chat.item;

import com.sunny.youyun.activity.chat.config.ChatConfig;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.model.data_item.Message;

/**
 * Created by Sunny on 2017/10/6 0006.
 */

public class MessageItemOther extends Message implements MultiItemEntity {
    public MessageItemOther(Builder builder) {
        super(builder);
    }

    public MessageItemOther(Message message) {
        super(new Message.Builder()
                .content(message.getContent())
                .createTime(message.getCreateTime())
                .fromUserId(message.getFromUserId())
                .id(message.getId())
                .toUserId(message.getToUserId())
                .updateTime(message.getUpdateTime())
                .user(message.getUser())
        );
    }

    @Override
    public int getItemType() {
        return ChatConfig.MESSAGE_ITEM_TYPE_OTHER;
    }
}
