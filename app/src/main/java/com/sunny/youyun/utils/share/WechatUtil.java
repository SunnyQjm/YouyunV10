package com.sunny.youyun.utils.share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.sunny.youyun.R;
import com.sunny.youyun.model.ShareContent;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Sunny on 2017/8/30 0030.
 */

public enum WechatUtil {
    INSTANCE;
    private Activity activity;
    private IWXAPI wechat;

    public static WechatUtil getInstance(@NonNull Activity activity) {
        if (INSTANCE.activity != activity) {
            INSTANCE.activity = activity;
        }
        //WeChat
        if (INSTANCE.wechat == null) {
            INSTANCE.wechat = WXAPIFactory.createWXAPI(activity.getApplication(), Constants.WE_CHAT_AP_ID, true);
            INSTANCE.wechat.registerApp(Constants.WE_CHAT_AP_ID);
        }
        return INSTANCE;
    }

    public IWXAPI getWechat() {
        return wechat;
    }

    private void shareWebpage(@NonNull final ShareContent shareContent, int scene){
        //初始化一个WXWebpageObject对象，填写url
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = shareContent.getShareUrl();

        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.title = shareContent.getShareTitle();
        msg.description = shareContent.getShareSummary();
        Bitmap thumb;
        if(shareContent.getThumb() == null){
            thumb = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.logo);
        } else {
            thumb = shareContent.getThumb();
        }
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;

        wechat.sendReq(req);
    }

    /**
     * 分享给微信好友
     * @param shareContent
     */
    public void shareToWechatSession(@NonNull final ShareContent shareContent){
       shareWebpage(shareContent, SendMessageToWX.Req.WXSceneSession);
    }

    /**
     * 分享到朋友圈
     * @param shareContent
     */
    public void shareToWechatTimeline(@NonNull final ShareContent shareContent){
        shareWebpage(shareContent, SendMessageToWX.Req.WXSceneTimeline);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
