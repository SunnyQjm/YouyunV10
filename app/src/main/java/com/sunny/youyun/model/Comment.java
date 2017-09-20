package com.sunny.youyun.model;

/**
 * 评论类
 * Created by Sunny on 2017/9/13 0013.
 */

public class Comment {
    private final int id;
    private final User user;
    private final int fileId;
    private final int userId;
    private final String comment;
    private final long createTime;


    private Comment(Builder builder) {
        id = builder.id;
        user = builder.user;
        fileId = builder.fileId;
        userId = builder.userId;
        comment = builder.comment;
        createTime = builder.createTime;
    }


    public int getUserId() {
        return userId;
    }

    public int getFileId() {
        return fileId;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getComment() {
        return comment;
    }


    public static final class Builder {
        private User user;
        private int fileId;
        private int userId;
        private String avatar;
        private String comment;
        private long createTime;
        private long commentDate;
        private int id;

        public Builder() {
        }

        public Builder user(User val) {
            user = val;
            return this;
        }

        public Builder fileId(int val) {
            fileId = val;
            return this;
        }

        public Builder userId(int val) {
            userId = val;
            return this;
        }

        public Builder avatar(String val) {
            avatar = val;
            return this;
        }


        public Builder comment(String val) {
            comment = val;
            return this;
        }

        public Builder createTime(long val) {
            createTime = val;
            return this;
        }

        public Builder commentDate(long val) {
            commentDate = val;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }

        public Builder id(int val) {
            id = val;
            return this;
        }
    }
}
