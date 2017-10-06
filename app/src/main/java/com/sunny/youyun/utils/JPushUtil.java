package com.sunny.youyun.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Sunny on 2017/10/4 0004.
 */

public class JPushUtil {
    public static void setTag(@NonNull Context context, @NonNull String tag){
        if(tag.equals("")){
            Logger.i("Tag is not allow NULL");
        }
        Set<String> tags = new HashSet<>();
        tags.add(tag);
        setTag(context, tags);
    }
    public static void setTag(@NonNull Context context, @NonNull Set<String> tags) {
        setTag(context, tags, (i, s, set) -> Logger.i("Set tag: " +
                i +
                "\n" +
                s +
                "\n" +
                set));
    }

    public static void setTag(@NonNull Context context, @NonNull Set<String> tags
            , TagAliasCallback callback) {
        JPushInterface.setTags(context, tags, callback);
    }
}
