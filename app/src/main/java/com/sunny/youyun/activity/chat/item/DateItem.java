package com.sunny.youyun.activity.chat.item;

import com.sunny.youyun.activity.chat.config.ChatConfig;
import com.sunny.youyun.base.entity.MultiItemEntity;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

public class DateItem implements MultiItemEntity{

    private long date = System.currentTimeMillis();

    private DateItem(Builder builder) {
        date = builder.date;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int getItemType() {
        return ChatConfig.MESSAGE_ITEM_TYPE_DATE;
    }

    public static final class Builder {
        private long date;

        public Builder() {
        }

        public Builder date(long val) {
            date = val;
            return this;
        }

        public DateItem build() {
            return new DateItem(this);
        }
    }
}
