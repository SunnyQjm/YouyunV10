package com.sunny.youyun.utils.bus;

import android.support.annotation.NonNull;

import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Sunny on 2017/8/19 0019.
 */

public enum ObjectPool {
    INSTANCE;

    public static ObjectPool getInstance() {
        return INSTANCE;
    }

    private final Map<String, Object> map = new ConcurrentHashMap<>();

    public boolean put(@NonNull final String key, @NonNull final org.litepal.crud.DataSupport o) {
        if (map.get(key) != null)
            return false;
        map.put(key, o);
        return true;
    }

    public <T extends InternetFile> T get(String key, T defaultVal) {
        Object o = map.get(key);
        if (o == null)
            return defaultVal;
        map.remove(key);
        return (T) o;
    }

    public <T extends User> T get(String key, T defaultVal) {
        Object o = map.get(key);
        if (o == null)
            return defaultVal;
        map.remove(key);
        return (T) o;
    }
}
