package com.sunny.youyun.utils.idling;


import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sunny on 2017/10/21 0021.
 */

public class SimpleCountingIdlingResource implements IdlingResource {
    private final String mResourceName;

    private final AtomicInteger counter = new AtomicInteger(0);
    private volatile ResourceCallback resourceCallback;

    public SimpleCountingIdlingResource(String mResourceName) {
        this.mResourceName = mResourceName;
    }


    @Override
    public String getName() {
        return mResourceName;
    }

    /**
     * 是否空闲
     * @return
     */
    @Override
    public boolean isIdleNow() {
        return counter.get() == 0;
    }

    /**
     * 注册空闲状态转换
     * @param callback
     */
    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    /**
     * 当开始异步请求时，counter值+1,表示有一个异步任务在执行
     */
    public void increment(){
        counter.getAndIncrement();
    }

    /**
     * 当一个异步请求完成时，counter - 1， 表示一个异步任务结束
     * 当counter值为0时表示空闲
     */
    public void decrement(){
        int countValue = counter.decrementAndGet();
        if(countValue == 0){        //如果所有异步任务都执行完毕，说明异步任务执行完毕，执行回调
            if(null != resourceCallback)
                resourceCallback.onTransitionToIdle();
        }

        if(countValue < 0)
            throw new IllegalArgumentException("counter must >= 0");
    }


}
