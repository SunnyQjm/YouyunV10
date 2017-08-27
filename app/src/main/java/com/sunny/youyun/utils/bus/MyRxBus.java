package com.sunny.youyun.utils.bus;

import com.sunny.youyun.internet.event.FileDownloadEvent;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by Sunny on 2017/8/9 0009.
 */

public enum MyRxBus {
    INSTANCE;
    public static MyRxBus getInstance(){
        return INSTANCE;
    }

    private final Subject<FileDownloadEvent> subject;
    private Map<String, Disposable> subscriptionMap = new HashMap<>();
    MyRxBus(){
        subject = PublishSubject.create();
    }

    public void post(FileDownloadEvent fileDownloadEvent){
        subject.onNext(fileDownloadEvent);
    }

    public void subscribe(String key, Consumer<FileDownloadEvent> next, Consumer<Throwable> error){
        Disposable disposable = subject.subscribe(next, error);
        subscriptionMap.put(key, disposable);
    }
    public void unSubscribe(String key){
        Disposable disposable = subscriptionMap.get(key);
        if(disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }



}
