package com.sunny.youyun.wifidirect.exception;

import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by Sunny on 2017/8/28 0028.
 */

public class FileCreateFailedException extends Exception{
    public FileCreateFailedException(String message) {
        super(message);
    }

    public FileCreateFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileCreateFailedException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public FileCreateFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
