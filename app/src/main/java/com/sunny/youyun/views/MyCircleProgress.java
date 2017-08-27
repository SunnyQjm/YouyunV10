package com.sunny.youyun.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.sunny.youyun.R;


/**
 * Created by Sunny on 2017/5/13 0013.
 */

public class MyCircleProgress extends View {

    // Properties
    private float progress = 0;
    private float strokeWidth = getResources().getDimension(R.dimen.default_stroke_width);
    private float backgroundStrokeWidth = getResources().getDimension(R.dimen.default_background_stroke_width);
    private float icon_stroke_width = getResources().getDimension(R.dimen.default_icon_stroke_width);
    private int color = Color.BLACK;
    private int backgroundColor = Color.GRAY;

    // Object used to draw
    private int startAngle = -90;
    private RectF rectF;
    private Paint backgroundPaint;
    private Paint foregroundPaint;

    private static final int PROGRESSING = 0;
    private static final int FINISH_SUCCESS = 1;
    private static final int FINISH_FAIL = 2;
    private static final int PAUSE = 3;

    private int state = PROGRESSING;

    private Paint tick_path_paint;
    private Paint play_path_paint;
    private Paint stop_path_paint;
    private int icon_color = Color.GREEN;

    private Path tick_path;
    private Path play_path;
    private Path stop_path;


    //region Constructor & Init Method
    public MyCircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();
        tick_path = new Path();
        play_path = new Path();
        stop_path = new Path();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyCircleProgress, 0, 0);
        //Reading values from the XML layout
        try {
            // Value
            progress = typedArray.getFloat(R.styleable.MyCircleProgress_progress_value, progress);
            // StrokeWidth
            strokeWidth = typedArray.getDimension(R.styleable.MyCircleProgress_progress_width, strokeWidth);
            backgroundStrokeWidth = typedArray.getDimension(R.styleable.MyCircleProgress_background_width, backgroundStrokeWidth);
            // Color
            color = typedArray.getInt(R.styleable.MyCircleProgress_progress_color, color);
            backgroundColor = typedArray.getInt(R.styleable.MyCircleProgress_background_color, backgroundColor);
            icon_color = typedArray.getColor(R.styleable.MyCircleProgress_icon_color, icon_color);
            icon_stroke_width = typedArray.getDimension(R.styleable.MyCircleProgress_icon_stroke_width, icon_stroke_width);
        } finally {
            typedArray.recycle();
        }

        // Init Background
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);

        // Init Foreground
        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);

        tick_path_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tick_path_paint.setColor(icon_color);
        tick_path_paint.setStyle(Paint.Style.STROKE);
        tick_path_paint.setStrokeWidth(icon_stroke_width);

        play_path_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        play_path_paint.setColor(icon_color);
        play_path_paint.setStyle(Paint.Style.STROKE);
        play_path_paint.setStrokeWidth(icon_stroke_width);

        stop_path_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stop_path_paint.setColor(icon_color);
        stop_path_paint.setStyle(Paint.Style.FILL);
    }
    //endregion

    //region Draw Method
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float angle;
        switch (state) {
            case PROGRESSING:       //加载中
                canvas.drawOval(rectF, backgroundPaint);
                angle = 360 * progress / 100;
                canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);
                canvas.drawPath(play_path, play_path_paint);
                break;
            case PAUSE:
                canvas.drawOval(rectF, backgroundPaint);
                angle = 360 * progress / 100;
                canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);
                canvas.drawPath(stop_path, stop_path_paint);
                break;
            case FINISH_SUCCESS:
//                canvas.drawOval(rectF, backgroundPaint);
//                canvas.drawArc(rectF, startAngle, 360, false, foregroundPaint);
                canvas.drawPath(tick_path, tick_path_paint);
                break;
            case FINISH_FAIL:
            default:

        }
    }
    //endregion

    //region Mesure Method
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = (strokeWidth > backgroundStrokeWidth) ? strokeWidth : backgroundStrokeWidth;
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2);
        tick_path.moveTo((rectF.left + rectF.right) / 4, (rectF.bottom + rectF.top) / 2);
        tick_path.lineTo(rectF.left + rectF.width() * 3 / 8, rectF.top + rectF.height() * 3 / 4);
        tick_path.lineTo(rectF.left + rectF.width() * 3 / 4, rectF.top + rectF.height() * 2 / 8);

        play_path.moveTo(rectF.left + rectF.width() * 3 / 8, rectF.top + rectF.height() / 4);
        play_path.lineTo(rectF.left + rectF.width() * 3 / 8, rectF.top + rectF.height() * 3 / 4);
        play_path.moveTo(rectF.left + rectF.width() * 5 / 8, rectF.top + rectF.height() / 4);
        play_path.lineTo(rectF.left + rectF.width() * 5 / 8, rectF.top + rectF.height() * 3 / 4);

        stop_path.moveTo(rectF.left + rectF.width() * 3 / 8, rectF.top + rectF.height() / 4);
        stop_path.lineTo(rectF.left + rectF.width() * 3 / 8, rectF.top + rectF.height() * 3 / 4);
        stop_path.lineTo(rectF.left + rectF.width() * 6 / 8, rectF.top + rectF.height() / 2);
        stop_path.close();
    }
    //endregion

    //region Method Get/Set
    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = (progress <= 100) ? progress : 100;
        if (this.progress != 100) {
            state = PROGRESSING;
        } else {
            state = FINISH_SUCCESS;
            if(onCircleProcessClickListener != null)
                onCircleProcessClickListener.finish();
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                changeState();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void changeState() {
        System.out.println(state);
        switch (state){
            case PROGRESSING:
                state = PAUSE;
                if(onCircleProcessClickListener != null)
                    onCircleProcessClickListener.pause();
                break;
            case PAUSE:
                state = PROGRESSING;
                if(onCircleProcessClickListener != null)
                    onCircleProcessClickListener.beginProgress();
                break;
            case FINISH_SUCCESS:
                break;
            case FINISH_FAIL:
                break;
        }
        invalidate();
    }


    public float getProgressBarWidth() {
        return strokeWidth;
    }

    public void setProgressBarWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        foregroundPaint.setStrokeWidth(strokeWidth);
        requestLayout();//Because it should recalculate its bounds
        invalidate();
    }

    public float getBackgroundProgressBarWidth() {
        return backgroundStrokeWidth;
    }

    public void setBackgroundProgressBarWidth(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
        requestLayout();//Because it should recalculate its bounds
        invalidate();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        foregroundPaint.setColor(color);
        invalidate();
        requestLayout();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
        requestLayout();
    }
    //endregion

    //region Other Method

    /**
     * Set the progress with an animation.
     * Note that the {@link ObjectAnimator} Class automatically set the progress
     *
     * @param progress The progress it should animate to it.
     */
    public void setProgressWithAnimation(float progress) {
        setProgressWithAnimation(progress, 1500);
    }

    /**
     * Set the progress with an animation.
     * Note that the {@link ObjectAnimator} Class automatically set the progress
     *
     * @param progress The progress it should animate to it.
     * @param duration The length of the animation, in milliseconds.
     */
    public void setProgressWithAnimation(float progress, int duration) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
    //endregion

    private OnCircleProcessClickListener onCircleProcessClickListener;

    public void setOnCircleProcessClickListener(OnCircleProcessClickListener onCircleProcessClickListener) {
        this.onCircleProcessClickListener = onCircleProcessClickListener;
    }

    public interface OnCircleProcessClickListener {
        void beginProgress();
        void pause();
        void finish();
    }
}
