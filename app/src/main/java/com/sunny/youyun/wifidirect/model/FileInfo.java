package com.sunny.youyun.wifidirect.model;

public class FileInfo {
    private final String path;
    private final String name;
    private final long size;

    private FileInfo(Builder builder) {
        path = builder.path;
        name = builder.name;
        size = builder.size;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public static final class Builder {
        private String path;
        private String name;
        private long size;

        public Builder() {
        }

        public Builder path(String val) {
            path = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder size(long val) {
            size = val;
            return this;
        }

        public FileInfo build() {
            return new FileInfo(this);
        }
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
