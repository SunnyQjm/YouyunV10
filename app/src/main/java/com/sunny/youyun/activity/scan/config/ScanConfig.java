package com.sunny.youyun.activity.scan.config;

/**
 * Created by Sunny on 2017/8/11 0011.
 */

public class ScanConfig {
    public static final String SCAN_RESULT = "scan stringResult";

    public static final int SCAN_RESULT_TYPE_USER_INFO = 0;
    public static final int SCAN_RESULT_TYPE_FILE_INFO = 1;
    public static final int SCAN_RESULT_TYPE_URL = 2;
    public static final int INVALID_TYPE = 3;
    private static final String divider = "#$#";

    public static String createData(int userId) {
        return String.valueOf(SCAN_RESULT_TYPE_USER_INFO) +
                divider +
                userId;
    }

    public static String createData(int fileId, String identifyCode){
        return String.valueOf(SCAN_RESULT_TYPE_FILE_INFO)
                + divider +
                fileId +
                divider +
                identifyCode;
    }

    public static String createData(String url){
        return String.valueOf(SCAN_RESULT_TYPE_URL)
                + divider +
                url;
    }

    /**
     * 获取类型
     * @param result
     * @return
     */
    public static int getType(String result){
        String[] results = result.split(divider);
        if(results.length == 0)
            return INVALID_TYPE;
        else
            return Integer.parseInt(results[0]);
    }

    public static String[] getContent(String result){
        String[] results = result.split(divider);
        if(results.length == 0)
            return null;
        String[] temp = new String[results.length - 1];
        System.arraycopy(results, 1, temp, 0, temp.length);
        return temp;
    }
}
