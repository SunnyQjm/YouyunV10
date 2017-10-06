package com.sunny.youyun.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunny.youyun.R;
import com.sunny.youyun.utils.DensityUtil;
import com.sunny.youyun.views.interfaces.LineMenu;

/**
 * Created by Sunny on 2017/10/5 0005.
 */

public class ExpandableLineMenuItem extends LinearLayout implements LineMenu {

    private LineMenuItem lineMenuItem = null;
    private boolean openedAble = true;
    private boolean open = false;

    private static final int DEFAULT_MENU_HEIGHT = 50;
    private int menuHeight;
    @DrawableRes
    private int menuBG = R.drawable.ripple_bg_white;
    private int menuWidth = LayoutParams.MATCH_PARENT;

    public ExpandableLineMenuItem(Context context) {
        this(context, null);
    }

    public ExpandableLineMenuItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableLineMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandableLineMenuItem);
        open = ta.getBoolean(R.styleable.ExpandableLineMenuItem_open, false);
        openedAble = ta.getBoolean(R.styleable.ExpandableLineMenuItem_openedAble, true);
        menuHeight = ta.getDimensionPixelOffset(R.styleable.ExpandableLineMenuItem_menu_height,
                DensityUtil.dip2px(context, DEFAULT_MENU_HEIGHT));
        menuWidth = ta.getDimensionPixelOffset(R.styleable.ExpandableLineMenuItem_menu_width,
                LayoutParams.MATCH_PARENT);
        menuBG = ta.getResourceId(R.styleable.ExpandableLineMenuItem_menu_bg, menuBG);
        ta.recycle();

        lineMenuItem = new LineMenuItem(context, attrs, defStyleAttr);
        lineMenuItem.setBackgroundResource(menuBG);
        lineMenuItem.setOnClickListener(v -> {
            if (open) {       //关闭操作
                close();
            } else {       //打开操作
                open();
            }
        });


    }

    /**
     * 菜单栏展开
     */
    public void open() {
        if (!openedAble)
            return;
        open = true;
        ObjectAnimator.ofFloat(lineMenuItem.getRight_icon(),
                "rotation", 0f, 90f)
                .setDuration(300)
                .start();
        //除了菜单本身，全部隐藏
        for (int i = 0; i < getChildCount() - 1; i++) {
            getChildAt(i).setVisibility(VISIBLE);
        }
    }

    /**
     * 菜单栏关闭
     */
    public void close() {
        if (!openedAble)
            return;
        open = false;
        ObjectAnimator.ofFloat(lineMenuItem.getRight_icon(),
                "rotation", 90f, 0f)
                .setDuration(300)
                .start();
        for (int i = 0; i < getChildCount() - 1; i++) {
            getChildAt(i).setVisibility(GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        System.out.println("onMeasure");
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //为每一个子控件计算大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutParams layoutParams = new LayoutParams(menuWidth, menuHeight);
        addView(lineMenuItem, layoutParams);
        if (open) {
            open();
        } else {
            close();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //先部署菜单布局
        lineMenuItem.layout(0, 0, getWidth(), lineMenuItem.getMeasuredHeight());
        int totalHeight = lineMenuItem.getMeasuredHeight();

        //部署包含的View
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i);
            int left = childView.getMeasuredWidth() != getWidth() ?
                    getWidth() - childView.getMeasuredWidth() : 0;
            childView.layout(left, totalHeight, left + childView.getMeasuredWidth(),
                    totalHeight + childView.getMeasuredHeight());
            totalHeight += childView.getMeasuredHeight();
        }
    }


    @Override
    public ImageView getLeft_icon() {
        return lineMenuItem.getLeft_icon();
    }

    @Override
    public ImageView getRight_icon() {
        return lineMenuItem.getRight_icon();
    }

    @Override
    public TextView getTv_title() {
        return lineMenuItem.getTv_title();
    }

    @Override
    public TextView getTv_value() {
        return lineMenuItem.getTv_value();
    }

    @Override
    public void setLeftIcon(int res) {
        lineMenuItem.setLeftIcon(res);
    }

    @Override
    public void setRightIcon(int res) {
        lineMenuItem.setRightIcon(res);
    }

    @Override
    public void setRightIconVisible(int visible) {
        lineMenuItem.setRightIconVisible(visible);
    }

    @Override
    public void setLeftIconVisible(int visible) {
        lineMenuItem.setLeftIconVisible(visible);
    }

    @Override
    public void setTitleIconVisible(int visible) {
        lineMenuItem.setTitleIconVisible(visible);
    }

    @Override
    public void setValueIconVisible(int visible) {
        lineMenuItem.setValueIconVisible(visible);
    }

    @Override
    public void setTitle(String title) {
        lineMenuItem.setTitle(title);
    }

    @Override
    public void setValue(String value) {
        lineMenuItem.setValue(value);
    }
}
