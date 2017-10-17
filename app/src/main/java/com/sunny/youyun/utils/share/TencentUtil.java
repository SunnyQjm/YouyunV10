package com.sunny.youyun.utils.share;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.sunny.youyun.R;
import com.sunny.youyun.model.ShareContent;
import com.sunny.youyun.model.YouyunAPI;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

/**
 * Created by Sunny on 2017/8/25 0025.
 */

public enum TencentUtil {
    INSTANCE;

    public static TencentUtil getInstance(@NonNull Activity activity) {
        if (INSTANCE.activity != activity) {
            INSTANCE.activity = activity;
        }
        if (INSTANCE.tencent == null) {
            //QQ
            INSTANCE.tencent = Tencent.createInstance(Constants.QQ_APP_ID, activity.getApplication());

        }
        return INSTANCE;
    }

    private Tencent tencent;
    private IWXAPI wechat;
    private Activity activity;
    private static final String SCOPE = "get_simple_userinfo";

    public void shareToQQ(ShareContent shareContent, IUiListener listener) {
        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.getShareUrl());
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.getShareTitle());
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent.getShareSummary());
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareContent.getShareImageUrl());
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getString(R.string.app_name));
        tencent.shareToQQ(activity, bundle, listener);
    }


    public Tencent getTencent() {
        return tencent;
    }

    public void shareToQzon(ShareContent shareContent, IUiListener listener) {
        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.getShareUrl());
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.getShareTitle());
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent.getShareSummary());
        ArrayList<String> list = new ArrayList<>();
        list.add(shareContent.getShareImageUrl());
        bundle.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, list);
        tencent.shareToQzone(activity, bundle, listener);
    }

    public void login(IUiListener iUiListener) {
        if (YouyunAPI.isIsLogin() && YouyunAPI.getOpenId() != null
                && !YouyunAPI.getOpenId().equals("")) {
            tencent.setOpenId(YouyunAPI.getOpenId());
            tencent.setAccessToken(YouyunAPI.getAccessToken(),
                    String.valueOf(YouyunAPI.getExpiresIn()));
        }
        tencent.login(activity, SCOPE, iUiListener);
    }

    public void loginOut() {
        tencent.logout(activity);
    }
}
