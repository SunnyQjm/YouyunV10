package com.sunny.youyun.views.clip_image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.sunny.youyun.utils.DensityUtil;


/**
 * @author fucker
 */
public class ClipImageLayout extends RelativeLayout {

    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;

    private int mHorizontalPadding = 24;
    private float aspectRatio = 1;

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mZoomImageView = new ClipZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

        setHorizontalPadding(mHorizontalPadding);
        setAspectRatio(aspectRatio);
    }

    public void setImageBitmap(Bitmap bm) {
        mZoomImageView.setImageBitmap(bm);
    }

    public void setImagePath(String path) {
        int[] screen = DensityUtil.getScreenParams(getContext());
        int width = screen[0];
        int height = screen[1];

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float scale = (float) ((options.outWidth * 1.0 / width) < 1 ? 1
                : options.outWidth * 1.0 / width);
        float scale2 = (float) ((options.outHeight * 1.0 / height) < 1 ? 1
                : options.outHeight * 1.0 / height);
        scale = scale < scale2 ? scale2 : scale;//select a bigger scale number.
        options.outHeight = (int) (options.outHeight / scale);
        options.outWidth = (int) (options.outWidth / scale);
        options.inSampleSize = Math.round(scale);
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        setImageBitmap(bm);
    }

    /**
     * 设置左右边距的方法
     *
     * @param mHorizontalPadding dp
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
        // 计算padding的px
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
    }

    /**
     * 设置裁剪图像的纵横比
     *
     * @param aspectRatio = height / width
     */
    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        mZoomImageView.setAspectRatio(aspectRatio);
        mClipImageView.setAspectRatio(aspectRatio);
    }

    /**
     * 裁切图片,multiple  0~1.0f
     *
     * @return
     */
    public Bitmap clip(float multiple) {
        return mZoomImageView.clip(multiple);
    }

}
