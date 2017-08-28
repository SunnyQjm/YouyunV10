package com.sunny.youyun.utils;

import android.graphics.drawable.ColorDrawable;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.sunny.youyun.R;

/**
 * Created by Sunny on 2017/8/5 0005.
 */

public enum GlideOptions {
    INSTANCE;
    private final RequestOptions requestOptions = new RequestOptions()
            .centerCrop()
            .error(R.drawable.file)
            .placeholder(R.drawable.file);
    private final RequestOptions avatarOptions = new RequestOptions()
            .circleCrop()
            .error(R.drawable.icon_logo_round)
            .placeholder(new ColorDrawable(0x00ffffff));
    private final DrawableTransitionOptions crossFadeDrawableTransitionOptions = new DrawableTransitionOptions()
            .crossFade();
    public static GlideOptions getInstance(){
        return INSTANCE;
    }

    public DrawableTransitionOptions getCrossFadeDrawableTransitionOptions() {
        return crossFadeDrawableTransitionOptions;
    }

    public RequestOptions getRequestOptions() {
        return requestOptions;
    }

    public RequestOptions getAvatarOptions() {
        return avatarOptions;
    }
}
