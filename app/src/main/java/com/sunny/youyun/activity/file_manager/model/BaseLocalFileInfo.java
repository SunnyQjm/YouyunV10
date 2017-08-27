package com.sunny.youyun.activity.file_manager.model;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

public class BaseLocalFileInfo {
    private final String name;
    private final long size;
    private final long lastModifiedTime;
    private final String path;
    private final boolean isDirect;


    protected BaseLocalFileInfo(Builder builder) {
        name = builder.name;
        size = builder.size;
        lastModifiedTime = builder.lastModifiedTime;
        path = builder.path;
        isDirect = builder.isDirect;
    }

    @Override
    public String toString() {
        return "BaseLocalFileInfo{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", lastModifiedTime=" + lastModifiedTime +
                ", path='" + path + '\'' +
                ", isDirect=" + isDirect +
                '}';
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public String getPath() {
        return path;
    }

    public boolean isDirect() {
        return isDirect;
    }

    public static final class Builder {
        private String name;
        private long size;
        private long lastModifiedTime;
        private String path;
        private boolean isDirect;

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

        public Builder path(String val) {
            path = val;
            return this;
        }

        public Builder isDirect(boolean val) {
            isDirect = val;
            return this;
        }

        public BaseLocalFileInfo build() {
            return new BaseLocalFileInfo(this);
        }
    }
}
