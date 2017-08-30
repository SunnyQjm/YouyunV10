package com.sunny.youyun.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.utils.GsonUtil;
import com.sunny.youyun.utils.share.WechatUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by Sunny on 2017/8/30 0030.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        WechatUtil.getInstance(this)
                .getWechat().handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logger.i(GsonUtil.getInstance().toJson(baseReq));
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Logger.i(GsonUtil.getInstance().toJson(baseResp));
        finish();
    }
}
