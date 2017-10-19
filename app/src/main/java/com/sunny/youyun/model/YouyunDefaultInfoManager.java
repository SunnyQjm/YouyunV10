package com.sunny.youyun.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sunny.youyun.R;

/**
 * Created by Sunny on 2017/10/19 0019.
 */

public class YouyunDefaultInfoManager {
    private static String DEFAULT_FILE_DESCRIPTION = "";
    private static String DEFAULT_USER_SIGNATURE = "";

    public static void init(@NonNull Context context) {
        DEFAULT_FILE_DESCRIPTION = context.getString(R.string.share_from_youyun);
        DEFAULT_USER_SIGNATURE = context.getString(R.string.nothing_to_live);
    }

    public static String getDefaultFileDescription(String description) {
        if (description == null || description.equals(""))
            return DEFAULT_FILE_DESCRIPTION;
        return description;
    }

    public static String getDefaultUserSignature(String signature){
        if(signature == null || signature.equals(""))
            return DEFAULT_USER_SIGNATURE;
        return signature;
    }

}
