package com.sunny.youyun.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.sunny.youyun.R;

/**
 * Created by Sunny on 2017/8/26 0026.
 */

public class RichEditText extends AppCompatEditText {

    private static final int LEFT = 1, TOP = 2, RIGHT = 3, BOTTOM = 4;
    private int drawableHeight, drawableWidth;
    private Drawable drawable;
    private int drawableLocation;

    public RichEditText(Context context) {
        this(context, null);
    }

    public RichEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs, R.styleable.RichEditText);
        drawDrawable(drawable);
    }

    public void setDrawableRes(@DrawableRes int res){
        drawDrawable(getResources().getDrawable(res));
    }

    /**
     * draw drawable, include size and location
     */
    private void drawDrawable(Drawable drawable) {
        if (drawable != null) {    //if and only if user set drawable src, we need to draw
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (drawableWidth != 0 && drawableHeight != 0) {      //resize the drable
                drawable = new BitmapDrawable(
                        getResources(), resizeBitmap(bitmap, drawableWidth, drawableHeight));
            } else {
                drawable = new BitmapDrawable(
                        getResources(), Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true));
            }

            switch (drawableLocation){
                case LEFT:
                    this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    break;
                case TOP:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                    break;
                case RIGHT:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                    break;
                case BOTTOM:
                    this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
                    break;
            }
        }
    }

    /**
     * resize bitmap
     *
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        //getCount the original size
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //calculate the scale
        float scaleWidth = newWidth * 1.0f / width;
        float scaleHeight = newHeight * 1.0f / height;

        //generate Matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private void getAttrs(Context context, AttributeSet attrs, int[] richEditText) {
        TypedArray ta = context.obtainStyledAttributes(attrs, richEditText);
        drawableHeight = ta.getDimensionPixelSize(R.styleable.RichEditText_drawable_height, 0);
        drawableWidth = ta.getDimensionPixelSize(R.styleable.RichEditText_drawable_width, 0);
        drawable = ta.getDrawable(R.styleable.RichEditText_drawable_src);
        drawableLocation = ta.getInt(R.styleable.RichEditText_drawable_location, LEFT);
        ta.recycle();
    }
}
