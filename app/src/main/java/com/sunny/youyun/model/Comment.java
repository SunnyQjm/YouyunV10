package com.sunny.youyun.model;

/**
 * 评论类
 * Created by Sunny on 2017/9/13 0013.
 */

public class Comment {
    private final User user;
    private final String comment;
    private final long commentDate;

    private Comment(Builder builder) {
        user = builder.user;
        comment = builder.comment;
        commentDate = builder.commentDate;
    }


    public User getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }

    public long getCommentDate() {
        return commentDate;
    }

    public static final class Builder {
        private User user;
        private String comment;
        private long commentDate;

        public Builder() {
        }

        public Builder user(User val) {
            user = val;
            return this;
        }

        public Builder comment(String val) {
            comment = val;
            return this;
        }

        public Builder commentDate(long val) {
            commentDate = val;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }
}
