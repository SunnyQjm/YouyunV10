package com.sunny.youyun.utils;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by Sunny on 2017/8/24 0024.
 */

public class ApkInfoUtil {
    public static Drawable getIcon(@NonNull final Activity context, @NonNull final String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (pi == null)
            return null;
        ApplicationInfo ai = pi.applicationInfo;
        return ai.loadIcon(pm);
    }

    public static String getLabel(@NonNull final Activity activity, @NonNull final String path) {
        File file = new File(path);
        if(!file.exists())
            return "";
        PackageManager pm = activity.getPackageManager();
        PackageInfo pi = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if(pi == null)
            return file.getName();
        ApplicationInfo ai = pi.applicationInfo;
        return String.valueOf(ai.loadLabel(pm));
    }
}
