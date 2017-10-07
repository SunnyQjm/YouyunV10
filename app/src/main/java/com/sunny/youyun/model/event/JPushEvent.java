package com.sunny.youyun.model.event;

import com.sunny.youyun.model.User;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

public class JPushEvent {
    public static final String INSTANTCONTACT = "INSTANTCONTACT";
    public static final String STAR = "STAR";
    public static final String FOLLOW = "FOLLOW";
    public static final String COMMENT = "COMMENT";
    private String type = INSTANTCONTACT;
    private User fromUser;
    private String title;
    private String content;
    private String push_id;

    private JPushEvent(Builder builder) {
        setType(builder.type);
        setFromUser(builder.fromUser);
        setTitle(builder.title);
        setContent(builder.content);
        push_id = builder.push_id;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }

    public String getPush_id() {
        return push_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static final class Builder {
        private String type;
        private User fromUser;
        private String title;
        private String content;
        private String push_id;
        private String id;

        public Builder() {
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder fromUser(User val) {
            fromUser = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder content(String val) {
            content = val;
            return this;
        }

        public Builder push_id(String val) {
            push_id = val;
            return this;
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public JPushEvent build() {
            return new JPushEvent(this);
        }
    }
}
