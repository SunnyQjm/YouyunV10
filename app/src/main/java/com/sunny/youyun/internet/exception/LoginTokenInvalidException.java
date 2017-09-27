package com.sunny.youyun.internet.exception;

import java.io.IOException;

/**
 * Created by Sunny on 2017/9/27 0027.
 */

public class LoginTokenInvalidException extends IOException{
    public LoginTokenInvalidException(String message) {
        super(message);
    }

    public LoginTokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
