package com.sunny.youyun.activity.decim.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.utils.DensityUtil;
import com.sunny.youyun.utils.GlideOptions;

import java.util.List;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

public class DcimAdapter extends BaseQuickAdapter<FileItem, BaseViewHolder> {
    private final List<FileItem> mList;
    private final Context context;
    private final LayoutInflater inflater;
    private static int windowWidth;
    private static int itemWidth;

    public DcimAdapter(@NonNull final Context context, @NonNull final List<FileItem> mList) {
        super(R.layout.dcim_item, mList);
        this.mList = mList;
        this.context = context;
        windowWidth = DensityUtil.getScreenWidth(context);
        itemWidth = (windowWidth - 2 * DensityUtil.dip2px(context, 2)) / 3;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    protected void convert(BaseViewHolder helper, FileItem item) {
        Glide.with(context)
                .load(item.getPath())
                .apply(GlideOptions
                        .getInstance().getTranspantOptions())
                .transition(GlideOptions
                        .getInstance().getCrossFadeDrawableTransitionOptions())
                .into((ImageView) helper.getView(R.id.img_photo));
//        AbsListView.LayoutParams param = new AbsListView.LayoutParams(DcimAdapter.itemWidth, DcimAdapter.itemWidth);
    }

}
