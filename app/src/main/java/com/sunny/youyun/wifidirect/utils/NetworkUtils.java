package com.sunny.youyun.wifidirect.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Created by Sunny on 2017/8/3 0003.
 */

public class NetworkUtils {
    public static boolean setWifiEnable(Context context, boolean enable){
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wm == null)
            return false;
        if(enable != wm.isWifiEnabled()){
            wm.setWifiEnabled(enable);
        }
        return wm.isWifiEnabled();
    }
}
