package com.sunny.youyun.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.model.data_item.FindingModel;
import com.sunny.youyun.model.InternetFile;

/**
 * Created by Sunny on 2017/8/20 0020.
 */

public class GlideUtils {

    public static void setImage(Context context, ImageView imageView, FindingModel findingModel) {
        int resId = FileTypeUtil.getIconIdByFileName(findingModel.getName());
        Glide.with(context)
                .load(resId)
                .apply(GlideOptions
                        .getInstance().getRequestOptions())
                .into(imageView);
    }

    public static void setImage(Context context, ImageView imageView, InternetFile internetFile) {
        int resId = FileTypeUtil.getIconIdByFileName(internetFile.getName());
        if (FileTypeUtil.judgeIsVideoPhoto(resId) && internetFile.getPath() != null &&
                !internetFile.getPath().equals("")) {
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

    /**
     * 加载url到ImageView中
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void load(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .apply(GlideOptions
                        .getInstance().getAvatarOptions())
                .transition(GlideOptions
                        .getInstance().getCrossFadeDrawableTransitionOptions())
                .into(imageView);
    }

    public static void load(Context context, ImageView imageView, @DrawableRes int res) {
        Glide.with(context)
                .load(res)
                .apply(GlideOptions
                        .getInstance().getAvatarOptions())
                .transition(GlideOptions
                        .getInstance().getCrossFadeDrawableTransitionOptions())
                .into(imageView);
    }

    public static void setImage(Context mContext, ImageView view, FileItem fileItem) {
        int resId = FileTypeUtil.getIconIdByFileName(fileItem.getName());
        if (FileTypeUtil.judgeIsVideoPhoto(resId) && fileItem.getPath() != null &&
                !fileItem.getPath().equals("")) {
            Glide.with(mContext)
                    .load(fileItem.getPath())
                    .apply(GlideOptions
                            .getInstance().getRequestOptions())
                    .transition(GlideOptions
                            .getInstance().getCrossFadeDrawableTransitionOptions())
                    .into(view);
        } else {
            Glide.with(mContext)
                    .load(resId)
                    .apply(GlideOptions
                            .getInstance().getRequestOptions())
                    .into(view);
        }
    }
}
