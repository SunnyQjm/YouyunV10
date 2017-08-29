package com.sunny.youyun.activity.decim.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.utils.DensityUtil;
import com.sunny.youyun.utils.GlideOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sunny on 2017/8/29 0029.
 */

public class DcimAdapter extends BaseAdapter {
    private final List<FileItem> mList;
    private final Context context;
    private final LayoutInflater inflater;
    private static int windowWidth;
    private static int itemWidth;
    private ViewGroup.LayoutParams layoutParams = null;

    public DcimAdapter(@NonNull final Context context, @NonNull final List<FileItem> mList) {
        this.mList = mList;
        this.context = context;
        windowWidth = DensityUtil.getScreenWidth(context);
        itemWidth = (windowWidth - 2 * DensityUtil.dip2px(context, 2)) / 3;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dcim_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FileItem fileItem = mList.get(position);
        Glide.with(context)
                .load(fileItem.getPath())
                .apply(GlideOptions
                        .getInstance().getTranspantOptions())
                .transition(GlideOptions
                        .getInstance().getCrossFadeDrawableTransitionOptions())
                .into(viewHolder.imgPhoto);
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(DcimAdapter.itemWidth, DcimAdapter.itemWidth);
        convertView.setLayoutParams(param);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img_photo)
        ImageView imgPhoto;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
