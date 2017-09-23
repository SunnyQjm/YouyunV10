package com.sunny.youyun.model.response_body;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class BaseResponseBody<T> implements Serializable {

    /**
     * success : true
     * status : 200
     * msg :
     */

    private boolean success;
    private int status;
    private int code = 0;
    private String msg;
    private T data;

    @Override
    public String toString() {
        return "BaseResponseBody{" +
                "success=" + success +
                ", status=" + status +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }
}
