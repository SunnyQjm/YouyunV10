package com.sunny.youyun.utils;

/**
 * Created by Sunny on 2017/4/9 0009.
 */

import android.util.Log;

import org.litepal.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * 写入文件管理类
 */
public class WriteFileManager {
    private static final String TAG = "DownLoadManager";

    public static String writeResponseBodyToDisk(ResponseBody body, String downloadName) {
        String path = FileUtils.getSDPath() + "/" + downloadName;
        Log.i(TAG, "WriteFileManager.startToWrite.path:" + path);

        File futureFile = new File(path);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        long fileSize = body.contentLength();
        LogUtil.d(TAG,"WriteFileManager.writeResponseBodyToDisk.fileSize:"+fileSize);

        try {
            try {
                byte[] fileReader = new byte[1024 * 1024];
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
//                    LogUtil.i(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                LogUtil.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                outputStream.flush();
                return path;
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return "";
        }

    }
}