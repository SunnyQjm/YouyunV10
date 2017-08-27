package com.sunny.youyun.model;

/**
 * Created by Sunny on 2017/8/7 0007.
 */

public class BaseFile {
    private final String name;
    private final long size;
    private final long lastModifiedTime;

    private BaseFile(Builder builder) {
        name = builder.name;
        size = builder.size;
        lastModifiedTime = builder.lastModifiedTime;
    }

    public static final class Builder {
        private String name;
        private long size;
        private long lastModifiedTime;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder size(long val) {
            size = val;
            return this;
        }

        public Builder lastModifiedTime(long val) {
            lastModifiedTime = val;
            return this;
        }

        public BaseFile build() {
            return new BaseFile(this);
        }
    }
}
