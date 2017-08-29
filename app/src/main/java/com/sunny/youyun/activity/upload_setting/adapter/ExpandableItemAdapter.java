package com.sunny.youyun.activity.upload_setting.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.config.ItemTypeConfig;
import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.activity.file_manager.item.MenuItem;
import com.sunny.youyun.base.adapter.BaseMultiItemQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.utils.GlideOptions;
import com.sunny.youyun.utils.TimeUtils;
import com.sunny.youyun.utils.Tool;

import java.util.List;

import static com.sunny.youyun.activity.file_manager.config.ItemTypeConfig.TYPE_FILE_INFO;
import static com.sunny.youyun.activity.file_manager.config.ItemTypeConfig.TYPE_LEVEL_0;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private final Context context;

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ExpandableItemAdapter(Context context, List<MultiItemEntity> data) {
        super(data);
        this.context = context;
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_FILE_INFO, R.layout.file_info_item_for_delete);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                MenuItem menuItem = (MenuItem) item;
                helper.setText(R.id.tv_name, menuItem.getTitle());

                if (menuItem.isExpanded()) {
                    helper.setImageResource(R.id.img_icon, R.drawable.icon_file_arrow_expand);
                } else {
                    helper.setImageResource(R.id.img_icon, R.drawable.icon_file_arrow_close);
                }

                //set view content
                helper.itemView.setOnClickListener(v -> {
                    int pos = helper.getAdapterPosition();
                    if (menuItem.isExpanded()) {
                        collapse(pos, false);
                    } else {
                        expand(pos, false);
                    }
                });

                break;

            case TYPE_FILE_INFO:
                FileItem fileItem = (FileItem) item;
                helper.setText(R.id.tv_name, fileItem.getName())
                        .setText(R.id.tv_size, Tool.convertToSize(fileItem.getSize()))
                        .setText(R.id.tv_time, TimeUtils.returnTime(fileItem.getLastModifiedTime()))
                        .addOnClickListener(R.id.img_delete);

                int resId = ((FileItem) item).getResId();
                if (resId == -1) {
                    Glide.with(context)
                            .load(fileItem.getPath())
                            .apply(GlideOptions
                                    .getInstance().getRequestOptions())
                            .transition(GlideOptions
                                    .getInstance().getCrossFadeDrawableTransitionOptions())
                            .into((ImageView) helper.getView(R.id.img_icon));
                } else {
                    Glide.with(context)
                            .load(resId)
                            .apply(GlideOptions
                            .getInstance().getRequestOptions())
                            .into((ImageView) helper.getView(R.id.img_icon));
                }
                break;
        }
    }

    public boolean removeItem(int position){
        MultiItemEntity target = getItem(position);
        if(target == null)
            return false;
        //get the item's parent position
        int cp = getParentPosition(target);
        MenuItem menuItem = (MenuItem) getItem(cp);
        if(menuItem == null)
            return false;
        menuItem.removeSubItem(position - cp - 1);
        remove(position);
        if (position >= 0 && getData().size() >= position &&
                getItemViewType(position - 1) == ItemTypeConfig.TYPE_LEVEL_0 &&
                (getData().size() == position || getItemViewType(position) == ItemTypeConfig.TYPE_LEVEL_0)) {
            remove(position - 1);
        }
        notifyDataSetChanged();
        return true;
    }
}
