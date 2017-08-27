package com.sunny.youyun.wifidirect.utils;

import com.sunny.youyun.wifidirect.event.FileTransEvent;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Sunny on 2017/8/11 0011.
 */

public enum TransRxBus {
    INSTANCE;
    public static TransRxBus getInstance(){
        return INSTANCE;
    }

    private final Subject<FileTransEvent> subject;
    private Map<String, Disposable> subscriptionMap = new HashMap<>();
    TransRxBus() {
        subject = PublishSubject.create();
    }

    public void post(FileTransEvent fileTransEvent){
        System.out.println(GsonUtil.getInstance().toJson(fileTransEvent));
        System.out.println(subject.hasObservers());
        subject.onNext(fileTransEvent);
    }

    public void subscribe(String key, Consumer<FileTransEvent> next, Consumer<Throwable> error){
        Disposable disposable = subject.subscribe(next, error);
        subscriptionMap.put(key, disposable);
    }
    public void unSubscribe(String key){
        Disposable disposable = subscriptionMap.get(key);
        if(disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }


}
