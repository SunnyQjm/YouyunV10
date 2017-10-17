package com.sunny.youyun.model.result;

/**
 * Created by Sunny on 2017/10/17 0017.
 */

public class ScanResult {
    private final int type;
    private final String data;
    private final String data2;


    public static final int TYPE_USER = 0;
    public static final int TYPE_FILE = 1;
    public static final int TYPE_URL = 2;
    public static final int TYPE_LOCAL_TRANS = 3;

    private ScanResult(Builder builder) {
        type = builder.type;
        data = builder.data;
        data2 = builder.data2;
    }

    public int getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public String getData2() {
        return data2;
    }

    public static final class Builder {
        private int type;
        private String data;
        private String data2;

        public Builder() {
        }

        public Builder type(int val) {
            type = val;
            return this;
        }

        public Builder data(String val) {
            data = val;
            return this;
        }

        public Builder data2(String val) {
            data2 = val;
            return this;
        }

        public ScanResult build() {
            return new ScanResult(this);
        }
    }
}
