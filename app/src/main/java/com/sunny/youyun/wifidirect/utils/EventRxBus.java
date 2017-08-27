package com.sunny.youyun.wifidirect.utils;

import com.sunny.youyun.wifidirect.event.BaseEvent;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Sunny on 2017/8/13 0013.
 */

public enum  EventRxBus {
    INSTANCE;
    public static EventRxBus getInstance(){
        return INSTANCE;
    }

    private final Subject<BaseEvent> subject = PublishSubject.create();
    private Map<String, Disposable> subscriptionMap = new HashMap<>();

    public void post(BaseEvent event){
        subject.onNext(event);
    }

    public void subscribe(String key, Consumer<BaseEvent> next, Consumer<Throwable> error){
        Disposable disposable = subject.subscribe(next, error);
        subscriptionMap.put(key, disposable);
    }

    public void unSubscribe(String key){
        Disposable disposable = subscriptionMap.get(key);
        if(disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }
}
