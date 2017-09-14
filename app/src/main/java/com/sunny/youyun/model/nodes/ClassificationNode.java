package com.sunny.youyun.model.nodes;

import android.support.annotation.DrawableRes;

/**
 * 分类节点
 * Created by Sunny on 2017/9/13 0013.
 */

public class ClassificationNode {
    @DrawableRes
    private final int iconRes;
    private final String name;

    private ClassificationNode(Builder builder) {
        iconRes = builder.iconRes;
        name = builder.name;
    }


    public String getName() {
        return name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public static final class Builder {
        private int iconRes;
        private String name;

        public Builder() {
        }

        public Builder iconRes(int val) {
            iconRes = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public ClassificationNode build() {
            return new ClassificationNode(this);
        }
    }
}
