package com.sunny.youyun.model.event;

/**
 * Created by Sunny on 2017/9/5 0005.
 */

public class MultiSelectEvent {
    public final Operator operator;

    public MultiSelectEvent(Operator operator) {
        this.operator = operator;
    }

    public enum Operator{
        SHOW, HIDE, DELETE, SELECT_ALL, CANCEL
    }
}
