package com.sunny.youyun.activity.person_file_manager.item;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

public class PathItem {
    private final String path;
    private final String parentId;

    private PathItem(Builder builder) {
        path = builder.path;
        parentId = builder.parentId;
    }

    public String getParentId() {
        return parentId;
    }

    public String getPath() {
        return path;
    }

    public static final class Builder {
        private String path;
        private String parentId;

        public Builder() {
        }

        public Builder path(String val) {
            path = val;
            return this;
        }

        public Builder parentId(String val) {
            parentId = val;
            return this;
        }

        public PathItem build() {
            return new PathItem(this);
        }
    }
}
