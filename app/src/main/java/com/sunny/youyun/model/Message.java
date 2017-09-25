package com.sunny.youyun.model;

/**
 * 消息Model
 *
 * Created by Sunny on 2017/9/25 0025.
 */

public class Message {
    private final User user;
    private final String content;
    private final long createTime;

    protected Message(Builder builder) {
        user = builder.user;
        content = builder.content;
        createTime = builder.createTime;
    }


    public long getCreateTime() {
        return createTime;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public static final class Builder {
        private User user;
        private String content;
        private long createTime;

        public Builder() {
        }

        public Builder user(User val) {
            user = val;
            return this;
        }

        public Builder content(String val) {
            content = val;
            return this;
        }

        public Builder createTime(long val) {
            createTime = val;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
