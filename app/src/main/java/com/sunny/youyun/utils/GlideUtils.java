package com.sunny.youyun.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunny.youyun.model.InternetFile;

/**
 * Created by Sunny on 2017/8/20 0020.
 */

public class GlideUtils {
    public static void setImage(Context context, ImageView imageView, InternetFile internetFile){
        int resId  = FileTypeUtil.getIconByFileNameWithoutVideoPhoto(internetFile.getName());
        if (resId == -1) {
            Glide.with(context)
                    .load(internetFile.getPath())
                    .apply(GlideOptions
                            .getInstance().getRequestOptions())
                    .transition(GlideOptions
                            .getInstance().getCrossFadeDrawableTransitionOptions())
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(resId)
                    .apply(GlideOptions
                            .getInstance().getRequestOptions())
                    .into(imageView);
        }
    }
}
