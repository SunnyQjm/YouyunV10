package com.sunny.youyun.wifidirect.base;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/3 0003.
 */

public abstract class BaseRunnable implements Runnable {
    protected boolean isStop = false;

    public boolean isStop() {
        return isStop;
    }

    protected abstract void stop() throws IOException;
}
