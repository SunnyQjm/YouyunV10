package com.sunny.youyun.activity.file_manager.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Sunny on 2017/8/3 0003.
 */

public class AppInfo {
    private final String appName;
    private final Drawable icon;
    private final String path;
    private final long lastModified;
    private final long size;

    protected AppInfo(Builder builder) {
        appName = builder.appName;
        icon = builder.icon;
        path = builder.path;
        lastModified = builder.lastModified;
        size = builder.size;
    }

    public String getAppName() {
        return appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public long getLastModified() {
        return lastModified;
    }

    public static final class Builder {
        private String appName;
        private Drawable icon;
        private String path;
        private long lastModified;
        private long size;

        public Builder() {
        }

        public Builder appName(String val) {
            appName = val;
            return this;
        }

        public Builder icon(Drawable val) {
            icon = val;
            return this;
        }

        public Builder path(String val) {
            path = val;
            return this;
        }

        public Builder lastModified(long val) {
            lastModified = val;
            return this;
        }

        public Builder size(long val) {
            size = val;
            return this;
        }

        public AppInfo build() {
            return new AppInfo(this);
        }
    }
}
