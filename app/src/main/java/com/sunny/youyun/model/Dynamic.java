package com.sunny.youyun.model;

/**
 * Created by Sunny on 2017/9/10 0010.
 */

public class Dynamic {
    private final String avatar;
    private final int mode;
    private final String fileName;
    private final long date;

    private Dynamic(Builder builder) {
        avatar = builder.avatar;
        mode = builder.mode;
        fileName = builder.fileName;
        date = builder.date;
    }

    public String getFileName() {
        return fileName;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getMode() {
        return mode;
    }

    public long getDate() {
        return date;
    }

    public static final class Builder {
        private String avatar;
        private int mode;
        private String fileName;
        private long date;

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
    }
}
