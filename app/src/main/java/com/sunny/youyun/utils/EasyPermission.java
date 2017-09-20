package com.sunny.youyun.utils;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

/**
 * Created by Sunny on 2017/8/19 0019.
 */

public class EasyPermission {
    public static void checkAndRequestREAD_WRITE_EXTENAL(@NonNull Activity context, @NonNull OnPermissionRequestListener mListener){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissionUtil.getInstance(context)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(aBoolean -> {
                        if(aBoolean){
                            mListener.success();
                            Logger.i("permissions", Manifest.permission.WRITE_EXTERNAL_STORAGE + ": 获取成功" );
                        } else {
                            mListener.fail();
                            Logger.i("permissions", Manifest.permission.WRITE_EXTERNAL_STORAGE + ": 获取失败" );
                        }
                    });
        }
    }

    public interface OnPermissionRequestListener{
        void success();
        void fail();
    }
}
