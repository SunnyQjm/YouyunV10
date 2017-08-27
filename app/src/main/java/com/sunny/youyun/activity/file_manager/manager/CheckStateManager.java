package com.sunny.youyun.activity.file_manager.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class manager the select state for each item
 * Created by Sunny on 2017/8/6 0006.
 */

public enum CheckStateManager {
    INSTANCE;

    public static CheckStateManager getInstance() {
        return INSTANCE;
    }

    private final Map<String, Boolean> checkedState = new HashMap<>();
    private final List<String> checkedPath = new ArrayList<>();

    public static void init() {
        INSTANCE.checkedState.clear();
        INSTANCE.checkedPath.clear();
    }

    public Map<String, Boolean> getCheckedState() {
        return checkedState;
    }

    /**
     * if the key not exist, return null
     * else return the value
     *
     * @param path
     * @return
     */
    public Boolean get(final String path) {
        return checkedState.get(path);
    }

    /**
     *
     * @param path
     * @param isChecked
     */
    public synchronized void put(final String path, final boolean isChecked) {
        checkedState.put(path, isChecked);
        if (isChecked && !checkedPath.contains(path)) {
            checkedPath.add(path);
        } else {
            checkedPath.remove(path);
        }
    }

    public int getResultNum(){
        return checkedPath.size();
    }
    /**
     * return the checked result
     * @return
     */
    public String[] result(){
        return checkedPath.toArray(new String[checkedPath.size()]);
    }
}
