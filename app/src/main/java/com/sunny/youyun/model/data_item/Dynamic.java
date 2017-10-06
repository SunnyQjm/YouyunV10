package com.sunny.youyun.model.data_item;

import com.sunny.youyun.model.InternetFile;

/**
 * Created by Sunny on 2017/9/10 0010.
 */

public class Dynamic {
    private final int id;
    private final int userId;
    private final int fileId;
    private final int type;
    private final InternetFile File;
    private final long createTime;

    Dynamic(Builder builder) {
        id = builder.id;
        userId = builder.userId;
        fileId = builder.fileId;
        type = builder.type;
        File = builder.File;
        createTime = builder.createTime;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getFileId() {
        return fileId;
    }

    public int getType() {
        return type;
    }

    public InternetFile getFile() {
        return File;
    }

    public long getCreateTime() {
        return createTime;
    }

    public static final class Builder {
        private String avatar;
        private int mode;
        private String fileName;
        private long date;
        private int id;
        private int userId;
        private int fileId;
        private int type;
        private InternetFile File;
        private long createTime;

        public Builder() {
        }

        public Builder avatar(String val) {
            avatar = val;
            return this;
        }

        public Builder mode(int val) {
            mode = val;
            return this;
        }

        public Builder fileName(String val) {
            fileName = val;
            return this;
        }

        public Builder date(long val) {
            date = val;
            return this;
        }

        public Dynamic build() {
            return new Dynamic(this);
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder userId(int val) {
            userId = val;
            return this;
        }

        public Builder fileId(int val) {
            fileId = val;
            return this;
        }

        public Builder type(int val) {
            type = val;
            return this;
        }

        public Builder File(InternetFile val) {
            File = val;
            return this;
        }

        public Builder createTime(long val) {
            createTime = val;
            return this;
        }
    }
}
