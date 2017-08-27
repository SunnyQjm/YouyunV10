package com.sunny.youyun.activity;

import com.sunny.youyun.utils.GsonUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by Sunny on 2017/6/4 0004.
 */

public class BaseUiListener implements IUiListener {
    @Override
    public void onComplete(Object o) {
        System.out.println(GsonUtil.getInstance().toJson(o));
    }

    @Override
    public void onError(UiError uiError) {
        System.out.println(uiError);
    }

    @Override
    public void onCancel() {
        System.out.println("cancel login");
    }
}
