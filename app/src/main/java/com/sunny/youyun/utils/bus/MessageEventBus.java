package com.sunny.youyun.utils.bus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

public enum MessageEventBus {
    INSTANCE;

    public static MessageEventBus getInstance() {
        //创建一个EventBus
        synchronized (MessageEventBus.class) {
            if (INSTANCE.eventBus == null) {
                INSTANCE.eventBus = new EventBus();
            }
        }
        return INSTANCE;
    }

    public void register(Object subscriber){
        eventBus.register(subscriber);
    }

    public void unregister(Object subscriber){
        eventBus.unregister(subscriber);
    }

    public void post(Object event){
        eventBus.post(event);
    }
    private EventBus eventBus = null;
}
