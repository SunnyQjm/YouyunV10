package com.sunny.youyun.wifidirect.event;

/**
 * Created by Sunny on 2017/8/13 0013.
 */

public class BaseEvent<T> {
    private final int code;
    private final String msg;
    private final T data;

    public BaseEvent(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    private BaseEvent(Builder<T> builder) {
        code = builder.code;
        msg = builder.msg;
        data = builder.data;
    }

    public static final class Builder<T> {
        private int code;
        private String msg;
        private T data;

        public Builder() {
        }

        public Builder code(int val) {
            code = val;
            return this;
        }

        public Builder msg(String val) {
            msg = val;
            return this;
        }

        public Builder data(T val) {
            data = val;
            return this;
        }

        public BaseEvent<T> build() {
            return new BaseEvent<>(this);
        }
    }
}
