package com.sunny.youyun.activity.file_manager.manager;

import android.util.SparseBooleanArray;

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

    private final SparseBooleanArray intCheckedState = new SparseBooleanArray();
    private final List<Position> integerList = new ArrayList<>();

    public static void init() {
        INSTANCE.checkedState.clear();
        INSTANCE.checkedPath.clear();
        INSTANCE.integerList.clear();
        INSTANCE.intCheckedState.clear();
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
        Boolean result = checkedState.get(path);
        return result == null ? false : result;
    }

    public Boolean get(final int position) {
        return intCheckedState.get(position);
    }

    /**
     * @param path
     * @param isChecked
     */
    public synchronized void put(final String path, final boolean isChecked) {
        checkedState.put(path, isChecked);
        if (!isChecked) {
            checkedPath.remove(path);
        } else if (!checkedPath.contains(path)) {
            checkedPath.add(path);
        }
    }

    public synchronized void put(final String path, final int position, boolean isChecked) {
        put(path, isChecked);
        put(position, isChecked);
    }

    public synchronized void put(final int position, final boolean isChecked) {
        intCheckedState.put(position, isChecked);
        if (!isChecked) {
            integerList.remove(new Position(position));
        } else if (!integerList.contains(new Position(position))) {
            integerList.add(new Position(position));
        }
    }

    public int getStringResultNum() {
        return checkedPath.size();
    }

    public int getIntResultNum() {
        return integerList.size();
    }

    /**
     * return the checked stringResult
     *
     * @return
     */
    public String[] stringResult() {
        return checkedPath.toArray(new String[checkedPath.size()]);
    }

    public Integer[] intResult() {
        Integer[] result = new Integer[integerList.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = integerList.get(i).position;
        }
        return result;
    }

    static class Position {
        int position;

        Position(int position) {
            this.position = position;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "position=" + position +
                    '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() != this.getClass())
                return false;
            Position position = (Position) obj;
            return this.position == position.position;
        }

        @Override
        public int hashCode() {
            return this.position;
        }
    }
}
