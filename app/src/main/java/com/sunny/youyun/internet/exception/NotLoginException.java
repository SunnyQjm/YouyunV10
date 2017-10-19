package com.sunny.youyun.internet.exception;

import java.io.IOException;

/**
 * Created by Sunny on 2017/10/19 0019.
 */

public class NotLoginException extends IOException{
    public NotLoginException(String message) {
        super(message);
    }

    public NotLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
