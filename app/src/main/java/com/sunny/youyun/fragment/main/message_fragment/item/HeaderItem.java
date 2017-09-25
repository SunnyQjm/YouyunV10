package com.sunny.youyun.fragment.main.message_fragment.item;

import android.support.annotation.DrawableRes;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.message_fragment.adapter.TypeConfig;

/**
 * Created by Sunny on 2017/9/25 0025.
 */

public class HeaderItem implements MultiItemEntity{
    private String text;
    @DrawableRes
    private int res;

    public HeaderItem(String text, int res) {
        this.text = text;
        this.res = res;
    }

    public int getRes() {
        return res;
    }

    public String getText() {
        return text;
    }

    @Override
    public int getItemType() {
        return TypeConfig.TYPE_HEADER;
    }
}
