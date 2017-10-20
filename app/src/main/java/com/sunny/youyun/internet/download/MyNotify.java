package com.sunny.youyun.internet.download;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.main.MainActivity;
import com.sunny.youyun.activity.main.config.MainActivityConfig;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import zlc.season.rxdownload3.core.Downloading;
import zlc.season.rxdownload3.core.Failed;
import zlc.season.rxdownload3.core.RealMission;
import zlc.season.rxdownload3.core.Status;
import zlc.season.rxdownload3.core.Succeed;
import zlc.season.rxdownload3.core.Suspend;
import zlc.season.rxdownload3.core.Waiting;
import zlc.season.rxdownload3.extension.ApkInstallExtension;
import zlc.season.rxdownload3.notification.NotificationFactory;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;

/**
 * Created by Sunny on 2017/10/20 0020.
 */

public class MyNotify implements NotificationFactory {
    private String channelId = "RxDownload";
    private String channelName = "RxDownload";

    private Map<RealMission, NotificationCompat.Builder> map = new HashMap<>();

    @NotNull
    @Override
    public Notification build(Context context, RealMission mission, Status status) {
        createChannelForOreo(context, channelId, channelName);
        NotificationCompat.Builder builder = get(mission, context);
        if (status instanceof Suspend) {
            return suspend(builder);
        } else if (status instanceof Waiting) {
            return waiting(builder);
        } else if (status instanceof Downloading) {
            return downloading(builder, status);
        } else if (status instanceof Failed) {
            return failed(builder);
        } else if (status instanceof Succeed) {
            return succeed(builder);
        } else if (status instanceof ApkInstallExtension.Installing) {
            return installing(builder);
        } else if (status instanceof ApkInstallExtension.Installed) {
            return installed(builder);
        } else {
            return new Notification();
        }
    }

    private Notification installed(NotificationCompat.Builder builder) {
        builder.setContentText("安装完成");
        dismissProgress(builder);
        return builder.build();
    }

    private Notification installing(NotificationCompat.Builder builder) {
        builder.setContentText("安装中");
        dismissProgress(builder);
        return builder.build();
    }

    private Notification suspend(NotificationCompat.Builder builder) {
        builder.setContentText("已暂停");
        dismissProgress(builder);
        return builder.build();
    }

    private Notification succeed(NotificationCompat.Builder builder) {
        builder.setContentText("下载成功");
        dismissProgress(builder);
        return builder.build();
    }

    private Notification downloading(NotificationCompat.Builder builder, Status status) {
        builder.setContentText("下载中");
        if (status.getChunkFlag()) {
            builder.setProgress(0, 0, true);
        } else {
            builder.setProgress((int) status.getTotalSize(), (int) status.getDownloadSize(), false);
        }
        return builder.build();
    }

    private Notification failed(NotificationCompat.Builder builder) {
        builder.setContentText("下载失败");
        dismissProgress(builder);
        return builder.build();
    }

    private Notification waiting(NotificationCompat.Builder builder) {
        builder.setContentText("等待中");
        builder.setProgress(0, 0, true);
        return builder.build();
    }


    private void dismissProgress(NotificationCompat.Builder builder) {
        builder.setProgress(0, 0, false);
    }

    private NotificationCompat.Builder get(RealMission mission, Context context) {
        NotificationCompat.Builder builder = map.get(mission);
        if (builder == null) {
            builder = createNotificationBuilder(mission, context);
            map.put(mission, builder);
        }

        return builder.setContentTitle(mission.getActual().getSaveName());
    }

    private NotificationCompat.Builder createNotificationBuilder(RealMission mission, Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivityConfig.LUNCH_TAG, MainActivityConfig.LUNCH_TAG_UPLOAD_DOWNLOAD);
        return new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_download)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(
                        context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle(mission.getActual().getSaveName());
    }

    private void createChannelForOreo(Context context, String channelId, String channelName) {
        if (SDK_INT >= O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager == null)
                return;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
