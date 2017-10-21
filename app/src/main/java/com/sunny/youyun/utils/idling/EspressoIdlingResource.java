package com.sunny.youyun.utils.idling;

import android.support.test.espresso.IdlingResource;

/**
 * Created by Sunny on 2017/10/21 0021.
 */

public enum  EspressoIdlingResource {
    INSTANCE;
    public static EspressoIdlingResource getInstance(){
        return INSTANCE;
    }
    private final String RESOURCE = "GLOBAL";
    private SimpleCountingIdlingResource mCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);

    public void increment(){
        System.out.println("increment");
        mCountingIdlingResource.increment();
    }

    public void decrement(){
        System.out.println("decrement");
        mCountingIdlingResource.decrement();
    }

    public IdlingResource getIdlingResource(){
        return mCountingIdlingResource;
    }


}
