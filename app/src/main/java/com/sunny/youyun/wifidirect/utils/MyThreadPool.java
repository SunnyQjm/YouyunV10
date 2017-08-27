package com.sunny.youyun.wifidirect.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Sunny on 2017/4/27 0027.
 */
public enum MyThreadPool {
    INSTANCE(Executors.newFixedThreadPool(10));

    private ExecutorService executorService;
    private volatile boolean shutdownJudge = false;

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
        shutdownJudge = true;
        executorService.shutdown();
    }

    public void shutDownNow() {
        shutdownJudge = true;
        executorService.shutdownNow();
    }

    public boolean isShutdownJudge() {
        return shutdownJudge;
    }
}
