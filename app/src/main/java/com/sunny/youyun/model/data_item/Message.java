package com.sunny.youyun.model.data_item;

import com.sunny.youyun.model.User;

import org.litepal.crud.DataSupport;

/**
 * 消息Model
 *
 * Created by Sunny on 2017/9/25 0025.
 */

public class Message extends DataSupport{

    /**
     * fromUserId : 1010
     * toUserId : 1001
     * content : 18340857280
     * id : 1004
     * createTime : 1507356322261
     * updateTime : 1507356322261
     */
    private int ownerId;
    private int targetId;
    private final int fromUserId;
    private final int toUserId;
    private final String content;
    private final int id;
    private final User user;
    private final long createTime;
    private final long updateTime;

    protected Message(Builder builder) {
        setOwnerId(builder.ownerId);
        setTargetId(builder.targetId);
        fromUserId = builder.fromUserId;
        toUserId = builder.toUserId;
        content = builder.content;
        id = builder.id;
        user = builder.user;
        createTime = builder.createTime;
        updateTime = builder.updateTime;
    }

    public User getUser() {
        return user;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public static final class Builder {
        private int fromUserId;
        private int toUserId;
        private String content;
        private int id;
        private User user;
        private long createTime;
        private long updateTime;
        private int ownerId;
        private int targetId;

        public Builder() {
        }

        public Builder fromUserId(int val) {
            fromUserId = val;
            return this;
        }

        public Builder toUserId(int val) {
            toUserId = val;
            return this;
        }

        public Builder content(String val) {
            content = val;
            return this;
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder user(User val) {
            user = val;
            return this;
        }

        public Builder createTime(long val) {
            createTime = val;
            return this;
        }

        public Builder updateTime(long val) {
            updateTime = val;
            return this;
        }

        public Message build() {
            return new Message(this);
        }

        public Builder ownerId(int val) {
            ownerId = val;
            return this;
        }

        public Builder targetId(int val) {
            targetId = val;
            return this;
        }
    }
}
