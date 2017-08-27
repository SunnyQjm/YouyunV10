package com.sunny.youyun.utils.share;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.sunny.youyun.R;
import com.sunny.youyun.model.ShareContent;
import com.tencent.connect.share.QQShare;
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
            INSTANCE.tencent = Tencent.createInstance(Constants.APP_ID, activity.getApplication());
        }
        return INSTANCE;
    }

    private Tencent tencent;
    private Activity activity;

    public void shareToQQ(ShareContent shareContent, IUiListener listener) {
        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.getShareUrl());
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.getShareTitle());
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent.getShareSummary());
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareContent.getShareImageUrl());
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getString(R.string.app_name));
        tencent.shareToQQ(activity, bundle, listener);
    }

    public void shareToQzon(    ShareContent shareContent, IUiListener listener) {
        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.getShareUrl());
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.getShareTitle());
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent.getShareSummary());
        ArrayList<String> list = new ArrayList<>();
        list.add(shareContent.getShareImageUrl());
        bundle.putStringArrayList(QQShare.SHARE_TO_QQ_IMAGE_URL, list);
        tencent.shareToQzone(activity, bundle, listener);
    }
}
