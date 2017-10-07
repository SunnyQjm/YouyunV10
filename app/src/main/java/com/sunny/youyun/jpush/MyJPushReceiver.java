package com.sunny.youyun.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sunny.youyun.model.User;
import com.sunny.youyun.model.data_item.Message;
import com.sunny.youyun.model.event.JPushEvent;
import com.sunny.youyun.model.manager.MessageManager;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.utils.GsonUtil;
import com.sunny.youyun.utils.MyNotifyUtil;
import com.sunny.youyun.utils.bus.MessageEventBus;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Sunny on 2017/5/29 0029.
 */

public class MyJPushReceiver extends BroadcastReceiver {
    private String TAG = "MyJPushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();

        System.out.println(TAG);
        System.out.println("Action: " + action);
        //SDK 向 JPush Server 注册所得到的注册 ID 。
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        }
        //收到了自定义消息 Push 。
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            Log.e(TAG, "收到了自定义消息。消息标题是：" + bundle.getString(JPushInterface.EXTRA_TITLE));
            Log.e(TAG, "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            Log.e(TAG, "收到了自定义消息。消息附加字段是：" + bundle.get(JPushInterface.EXTRA_EXTRA));
            Log.e(TAG, "收到了自定义消息。唯一标识消息的 ID是：" + bundle.getString(JPushInterface.EXTRA_MSG_ID));
            Log.e(TAG, "收到了自定义消息,类型为：" + bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE));

            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            JSONObject extras = null;
            try {
                extras = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                System.out.println(extras);
                String id = bundle.getString(JPushInterface.EXTRA_MSG_ID);
                String type = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
                User user = null;
                try {
                    user = GsonUtil.json2Bean(extras.getString("fromUser"), User.class);
                    if (user != null) {
                        if (MyNotifyUtil.getShowTag() != MyNotifyUtil.SHOW_TAG_CHATTING ||
                                MyNotifyUtil.getChattingId() != user.getId()) {
                            //未读消息+1
                            MessageManager.getInstance()
                                    .addCount(user.getId());
                        }

                        //收到新消息
                        if (type != null && type.equals(JPushEvent.INSTANTCONTACT)) {
                            MessageManager.getInstance()
                                    .put(user.getId(), new Message.Builder()
                                            .content(content)
                                            .fromUserId(user.getId())
                                            .toUserId(UserInfoManager.getInstance().getUserId())
                                            .createTime(System.currentTimeMillis())
                                            .user(user)
                                            .build());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JPushEvent jPushEvent = new JPushEvent.Builder()
                        .content(content)
                        .fromUser(user)
                        .title(title)
                        .type(type)
                        .push_id(id)
                        .build();
                //推送一个Event
                MessageEventBus.getInstance()
                        .post(jPushEvent);
                MyNotifyUtil.showChatNotify(context, jPushEvent);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

        }
        //收到了通知 Push。
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
            Log.e(TAG, "收到了通知");
            Log.e(TAG, "收到了通知。消息标题是：" + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
            Log.e(TAG, "收到了通知。附加字段是：" + bundle.getString(JPushInterface.EXTRA_ALERT));
            Log.e(TAG, "收到了通知。附加字段是：" + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里可以做些统计，或者做些其他工作
        }
        // 用户点击了通知以后的响应
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
            Log.e(TAG, "用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
//            Intent i = new Intent(context, TestActivity.class);  //自定义打开的界面
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
        } else {
            Log.e(TAG, "Unhandled intent - " + action);
        }
    }
}
