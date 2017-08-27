package com.sunny.youyun.utils;

import android.app.Activity;
import android.os.Build;

import com.github.mzule.activityrouter.router.Routers;
import com.sunny.youyun.App;
import com.sunny.youyun.IndexRouter;

/**
 * Created by Administrator on 2017/3/29/029.
 */

public class RouterUtils {
    public static void openForResult(Activity activity, String routerWithNoScheme, int requestCode, String... params) {
        String router = IndexRouter.IndexScheme + routerWithNoScheme;
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
        StringBuilder router = new StringBuilder(IndexRouter.IndexScheme + routerWithNoScheme);
        if (params != null) {
            for (String param : params) {
                router.append("/").append(param);
            }
        }
        System.out.println(router);
        Routers.open(context, router.toString());
        //5.0以下用老版本的切换动画
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            App.startAnim(context);
        }
    }
}
