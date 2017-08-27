package com.sunny.youyun.utils.bus;

import android.support.annotation.NonNull;

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

    public boolean put(@NonNull final String key, @NonNull final Object o) {
        if (map.get(key) != null)
            return false;
        map.put(key, o);
        return true;
    }

    public <T> T get(String key, T defaultVal) {
        Object o = map.get(key);
        if (o == null)
            return defaultVal;
        if (o.getClass() != defaultVal.getClass()) {
            return defaultVal;
        }
        map.remove(key);
        return (T)o;
    }
}
