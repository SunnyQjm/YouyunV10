package com.sunny.youyun.wifidirect.model;

/**
 * Created by Sunny on 2017/4/19 0019.
 */
public class SocketRequestBody<T> {
    private int code;
    private String msg;
    private T obj;

    public SocketRequestBody(int code, String msg, T obj) {

        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }

    public SocketRequestBody() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
