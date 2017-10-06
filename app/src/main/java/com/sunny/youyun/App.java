package com.sunny.youyun;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.download.FileDownloader;
import com.sunny.youyun.internet.upload.FileUploader;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.utils.JPushUtil;
import com.sunny.youyun.utils.MyThreadPool;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;
import com.sunny.youyun.wifidirect.model.TransLocalFile;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Sunny on 2017/5/9 0009.
 */

public class App extends Application {
    public static final String AppName = "Youyun";
    public static final List<TransLocalFile> mList_SendRecord = new CopyOnWriteArrayList<>();
    public static final List<TransLocalFile> mList_ReceiveRecord = new CopyOnWriteArrayList<>();
    public static final List<InternetFile> mList_UploadRecord = new CopyOnWriteArrayList<>();
    public static final List<InternetFile> mList_DownloadRecord = new CopyOnWriteArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();

        LitePal.initialize(this);
        APIManager.init(this);
        WifiDirectManager.init(this);
        //load user info from local
        UserInfoManager.init();
        int id = UserInfoManager.getInstance()
                .getUserInfo()
                .getId();
        System.out.println("id: " + id);
        //设置设备的 推送标识
        JPushUtil.setTag(this, String.valueOf(id));
        YouyunAPI.getInstance().bind(this);
        FileDownloader.bind(this);
        FileUploader.bind(this);
        MyThreadPool.getInstance().submit(() -> {
            JPushInit();
            LoggerInit();
            initList();
        });
    }

    private void LoggerInit() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private void JPushInit() {
        // 极光推送初始化
        JPushInterface.setDebugMode(true);
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
        builder.statusBarDrawable = R.drawable.music;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(1, builder);
        JPushInterface.init(this);

    }

    public static void startAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void finishAnim(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private static void initList() {
        mList_UploadRecord.addAll(DataSupport.where("fileTAG = ?", String.valueOf(InternetFile.TAG_UPLOAD)).find(InternetFile.class));
        System.out.println("上传文件记录：" + mList_UploadRecord.size());
        mList_DownloadRecord.addAll(DataSupport.where("fileTAG = ?", String.valueOf(InternetFile.TAG_DOWNLOAD)).find(InternetFile.class));
        System.out.println("下载文件记录：" + mList_DownloadRecord.size());
        mList_ReceiveRecord.addAll(DataSupport.where("fileTAG = ?", String.valueOf(TransLocalFile.TAG_RECEIVE)).find(TransLocalFile.class));
        System.out.println("快传接收记录：" + mList_ReceiveRecord.size());
        mList_SendRecord.addAll(DataSupport.where("fileTAG = ?", String.valueOf(TransLocalFile.TAG_SEND)).find(TransLocalFile.class));
        System.out.println("快传发送记录：" + mList_SendRecord.size());
    }
}
