package com.sunny.youyun.internet.exception;

/**
 * Created by Sunny on 2017/8/23 0023.
 */

public class FileDownloadException extends Exception{
    public FileDownloadException(String message) {
        super(message);
    }

    public FileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDownloadException(Throwable cause) {
        super(cause);
    }
}
