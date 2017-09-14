package com.sunny.youyun.model;

/**
 * Created by Sunny on 2017/9/13 0013.
 */

public class Collection {
    private final InternetFile internetFile;

    private Collection(Builder builder) {
        internetFile = builder.internetFile;
    }


    public InternetFile getInternetFile() {
        return internetFile;
    }

    public static final class Builder {
        private InternetFile internetFile;

        public Builder() {
        }

        public Builder internetFile(InternetFile val) {
            internetFile = val;
            return this;
        }

        public Collection build() {
            return new Collection(this);
        }
    }
}
