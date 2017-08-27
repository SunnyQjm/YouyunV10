package com.sunny.youyun.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Sunny on 2017/4/27 0027.
 */
public enum MyThreadPool {
    INSTANCE(Executors.newCachedThreadPool());

    private ExecutorService executorService;

    MyThreadPool(ExecutorService executorService) {
        this.executorService = executorService;
    }


    public static MyThreadPool getInstance() {
        return INSTANCE;
    }

    public void submit(Runnable task) {
        getInstance().executorService.submit(task);
    }

    public void shutDown() {
        executorService.shutdown();
    }

    public void shutDownNow() {
        executorService.shutdownNow();
    }

}
