package com.sunny.youyun.utils;

import java.text.DecimalFormat;

/**
 * Created by Sunny on 2017/5/7 0007.
 */
public class NumberUtils {

    private static DecimalFormat decimalFormat = new DecimalFormat(".00");

    /**
     * 取浮点数小数点后两位
     * @param f
     * @return
     */
    public static float returnFloatOfTwo(double f) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return Float.parseFloat(decimalFormat.format(f));
    }

    public static String bytesToStringSize(double f){
        float total = Float.parseFloat(decimalFormat.format(f / 1024));
        if(total > 1024) {
            total = Float.parseFloat(decimalFormat.format(total / 1024));
            return total + "MB";
        }
        return total + "KB";
    }

    /**
     * 返回形如 3.24M/5.63M  格式的字符串
     * @param fileSize 文件的比特大小
     * @param process   文件传输的进度
     * @return
     */
    public static String byteSizeToSizeAtio(long fileSize, float process) {
        float total = Float.parseFloat(decimalFormat.format(fileSize * 1.0 / 1024 / 1024));
        float alreadyTrans = Float.parseFloat(decimalFormat.format(total * process / 100f));
        return alreadyTrans + "M/" + total + "M";
    }
}
