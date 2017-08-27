package com.sunny.youyun.internet.upload;

/**
 * Created by Sunny on 2017/8/24 0024.
 */

public class FileUploadPosition {
    public int position;
    public int judge;


    @Override
    public String toString() {
        return "FileUploadPosition{" +
                "position=" + position +
                ", judge=" + judge +
                '}';
    }

    public FileUploadPosition(int position) {
        this(position, 0);
    }

    public FileUploadPosition(int position, int judge) {
        this.position = position;
        this.judge = judge;
    }
}
