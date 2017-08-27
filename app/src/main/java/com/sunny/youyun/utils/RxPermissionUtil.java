package com.sunny.youyun.utils;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * Created by Sunny on 2017/8/23 0023.
 */

public enum RxPermissionUtil {
    INSTANCE;
    public static RxPermissions getInstance(@NonNull Activity context) {
        return new RxPermissions(context);
    }
}
