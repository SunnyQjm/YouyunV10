package com.sunny.youyun.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;

import com.github.mzule.activityrouter.router.Routers;
import com.sunny.youyun.App;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.bus.ObjectPool;

/**
 * Created by Administrator on 2017/3/29/029.
 */

public class RouterUtils {
    public static void openForResult(Activity activity, String routerWithNoScheme, int requestCode, String... params) {
        String router = IntentRouter.IndexScheme + routerWithNoScheme;
        if (params != null) {
            for (String param : params) {
                router += "/" + param;
            }
        }
        Routers.openForResult(activity, router, requestCode);
        //5.0以下用老版本的切换动画
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            App.startAnim(activity);
        }
    }

    public static void open(Activity context, String routerWithNoScheme, String... params) {
        StringBuilder router = new StringBuilder(IntentRouter.IndexScheme + routerWithNoScheme);
        if (params != null) {
            for (String param : params) {
                router.append("/").append(param);
            }
        }
        System.out.println(router);
        Routers.open(context, router.toString());
        App.startAnim(context);
    }

    public static void openToFileDetailOnline(Activity activity, InternetFile internetFile){
        String uuid = UUIDUtil.getUUID();
        ObjectPool.getInstance().put(uuid, internetFile);
        RouterUtils.open(activity, IntentRouter.FileDetailOnlineActivity, uuid);
    }

    public static void openToUser(Activity activity, int userId){
        RouterUtils.open(activity, IntentRouter.PersonInfoActivity, String.valueOf(userId));
    }

    @SafeVarargs
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void openWithAnimation(Activity activity, Intent intent, Pair<View, String>... shares) {
        activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity, shares).toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SafeVarargs
    public static void openForResultWithAnimation(Activity activity, Intent intent, int requestCode, Pair<View, String>... shares) {
        activity.startActivityForResult(intent, requestCode,
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, shares).toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SafeVarargs
    public static void openForResultWithAnimation(Fragment fragment, Intent intent, int requestCode, Pair<View, String>... shares) {
        fragment.startActivityForResult(intent, requestCode,
                ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.getActivity(), shares).toBundle());
    }

    public static void openForResult(Fragment fragment, Intent intent, int requestCode) {
        fragment.startActivityForResult(intent, requestCode);
    }
}
