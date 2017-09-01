package com.sunny.youyun.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunny.youyun.R;
import com.sunny.youyun.utils.DensityUtil;
import com.sunny.youyun.utils.GeneratedId;

/**
 * Created by Sunny on 2017/8/16 0016.
 */

public class LineMenuItem extends RelativeLayout {

    private Context context = null;
    private String menuTitle;
    private String menuValue;
    private float menuTitleSize;
    @ColorInt
    private int menuTitleColor;
    private float menuValueSize;
    @ColorInt
    private int menuValueColor;
    @DrawableRes
    private int leftResource;
    private int rightResource;

    private boolean isRightIconVisible;
    private boolean isLeftIconVisible;

    private int leftIconSize;
    private int rightIconSize;

    private int leftMargin;
    private int rightMargin;

    private ImageView left_icon;
    private ImageView right_icon;
    private TextView tv_title;
    private TextView tv_value;

    private static final int DEFAULT_TEXT_SIZE = 14;
    private static final float DEFAULT_MARGIN = 13;

    private static final int DEFAULT_TITLE_COLOR = R.color.text_gray;
    private static final int DEFAULT_LEFT_RESOURCE = R.mipmap.logo;
    private static final int DEFAULT_RIGHT_RESOURCE = R.drawable.icon_arrow;
    private static final int DEFAULT_LEFT_ICON_SIZE = 24;
    private static final int DEFAULT_RIGHT_ICON_SIZE = 24;

    public LineMenuItem(Context context) {
        this(context, null);
    }

    public LineMenuItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        getAttr(context, attrs, defStyleAttr);
        initView();
    }

    public ImageView getLeft_icon() {
        return left_icon;
    }

    public ImageView getRight_icon() {
        return right_icon;
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public TextView getTv_value() {
        return tv_value;
    }

    private void getAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineMenuItem);
        menuTitle = ta.getString(R.styleable.LineMenuItem_menu_title);
        menuValue = ta.getString(R.styleable.LineMenuItem_menu_value);
        menuTitleSize = ta.getFloat(R.styleable.LineMenuItem_menu_title_size, DEFAULT_TEXT_SIZE);
        menuTitleColor = ta.getColor(R.styleable.LineMenuItem_menu_title_color,
                getResources().getColor(DEFAULT_TITLE_COLOR));
        menuValueSize = ta.getFloat(R.styleable.LineMenuItem_menu_value_size, DEFAULT_TEXT_SIZE);
        menuValueColor = ta.getColor(R.styleable.LineMenuItem_menu_value_color,
                getResources().getColor(DEFAULT_TITLE_COLOR));
        leftResource = ta.getResourceId(R.styleable.LineMenuItem_left_resource, DEFAULT_LEFT_RESOURCE);
        rightResource = ta.getResourceId(R.styleable.LineMenuItem_right_resource, DEFAULT_RIGHT_RESOURCE);
        leftIconSize = ta.getDimensionPixelSize(R.styleable.LineMenuItem_left_icon_size,
                DensityUtil.dip2px(context, DEFAULT_LEFT_ICON_SIZE));
        rightIconSize = ta.getDimensionPixelSize(R.styleable.LineMenuItem_right_icon_size,
                DensityUtil.dip2px(context, DEFAULT_RIGHT_ICON_SIZE));
        isRightIconVisible = ta
                .getBoolean(R.styleable.LineMenuItem_is_right_icon_visible, true);
        isLeftIconVisible = ta
                .getBoolean(R.styleable.LineMenuItem_is_left_icon_visible, false);
        leftMargin = ta.getDimensionPixelSize(R.styleable.LineMenuItem_left_margin, DensityUtil.dip2px(context, DEFAULT_MARGIN));
        rightMargin = ta.getDimensionPixelOffset(R.styleable.LineMenuItem_right_margin, DensityUtil.dip2px(context, DEFAULT_MARGIN));
        ta.recycle();
    }

    private void initView() {
        left_icon = new ImageView(context);
        left_icon.setImageResource(leftResource);
        left_icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        left_icon.setBackgroundResource(R.color.transparent);
        LayoutParams left_icon_param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        left_icon_param.addRule(CENTER_VERTICAL);
        left_icon_param.width = leftIconSize;
        left_icon_param.height = leftIconSize;
        left_icon_param.leftMargin = leftMargin;
        left_icon.setLayoutParams(left_icon_param);
        addView(left_icon, left_icon_param);
        if(!isLeftIconVisible){
            left_icon.setVisibility(GONE);
        }
        int left_icon_id = GeneratedId.generateViewId();
        left_icon.setId(left_icon_id);

        tv_title = new TextView(context);
        tv_title.setText(menuTitle);
        tv_title.setTextColor(menuTitleColor);
        tv_title.setTextSize(menuTitleSize);
        tv_title.setBackgroundResource(R.color.transparent);
        LayoutParams title_param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        title_param.addRule(CENTER_VERTICAL);
        title_param.leftMargin = leftMargin;
        title_param.addRule(RIGHT_OF, left_icon_id);
        tv_title.setLayoutParams(title_param);
        addView(tv_title, title_param);





        right_icon = new ImageView(context);
        right_icon.setImageResource(rightResource);
        right_icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        right_icon.setBackgroundResource(R.color.transparent);
        LayoutParams right_icon_param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        right_icon_param.addRule(CENTER_VERTICAL);
        right_icon_param.addRule(ALIGN_PARENT_RIGHT);
        right_icon_param.width = rightIconSize;
        right_icon_param.height = rightIconSize;
        right_icon_param.rightMargin = rightMargin;
        right_icon.setLayoutParams(right_icon_param);
        addView(right_icon, right_icon_param);
        int right_icon_id = GeneratedId.generateViewId();
        right_icon.setId(right_icon_id);

        tv_value = new TextView(context);
        tv_value.setText(menuValue);
        tv_value.setTextColor(menuValueColor);
        tv_value.setTextSize(menuValueSize);
        tv_value.setBackgroundResource(R.color.transparent);
        LayoutParams tv_value_param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_value_param.addRule(CENTER_VERTICAL);
        if (isRightIconVisible)
            tv_value_param.addRule(LEFT_OF, right_icon_id);
        else
            tv_value_param.addRule(ALIGN_PARENT_RIGHT);
        tv_value_param.rightMargin = DensityUtil.dip2px(context, DEFAULT_MARGIN);
        tv_value.setLayoutParams(tv_value_param);
        addView(tv_value, tv_value_param);

        if (isRightIconVisible)
            setRightIconVisible();
        else
            setRightIconInVisible();
    }


    public void setRightIconVisible() {
        if (right_icon != null)
            right_icon.setVisibility(VISIBLE);
    }

    public void setRightIconInVisible() {
        if (right_icon != null) {
            right_icon.setVisibility(INVISIBLE);
        }
    }


    public void setLeftIconVisible() {
        if (left_icon != null)
            left_icon.setVisibility(VISIBLE);
    }

    public void setLeftIconInVisible() {
        if (left_icon != null)
            left_icon.setVisibility(INVISIBLE);
    }

    public void setTitleVisible() {
        if (tv_title != null)
            tv_title.setVisibility(VISIBLE);
    }

    public void setTitleInVisible() {
        if (tv_title != null)
            tv_title.setVisibility(INVISIBLE);
    }

    public void setValueVisible() {
        if (tv_value != null)
            tv_value.setVisibility(VISIBLE);
    }

    public void setValueInvVsible() {
        if (tv_value != null)
            tv_value.setVisibility(INVISIBLE);
    }


    public void setTitle(String title) {
        if (tv_title != null)
            tv_title.setText(title);
    }

    public void setValue(String value) {
        if (tv_value != null)
            tv_value.setText(value);
    }
}
