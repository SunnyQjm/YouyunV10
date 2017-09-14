package com.sunny.youyun.activity.file_manager.adpater;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.item.AppInfoItem;
import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.activity.file_manager.item.MenuItem;
import com.sunny.youyun.activity.file_manager.manager.CheckStateManager;
import com.sunny.youyun.activity.file_manager.model.AppInfo;
import com.sunny.youyun.base.adapter.BaseMultiItemQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.utils.GlideOptions;
import com.sunny.youyun.utils.TimeUtils;
import com.sunny.youyun.utils.Tool;

import java.util.List;

import static com.sunny.youyun.activity.file_manager.config.ItemTypeConfig.TYPE_APPLICATION_INFO;
import static com.sunny.youyun.activity.file_manager.config.ItemTypeConfig.TYPE_FILE_INFO;
import static com.sunny.youyun.activity.file_manager.config.ItemTypeConfig.TYPE_LEVEL_0;

/**
 * Created by Sunny on 2017/8/4 0004.
 */

public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private final Context context;
    private final MVPBaseFragment.OnFragmentInteractionListener listener;

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     * @param mListener
     */
    public ExpandableItemAdapter(Context context, List<MultiItemEntity> data, MVPBaseFragment.OnFragmentInteractionListener mListener) {
        super(data);
        this.context = context;
        this.listener = mListener;
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_FILE_INFO, R.layout.file_info_item);
        addItemType(TYPE_APPLICATION_INFO, R.layout.file_info_item);
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
                        .setText(R.id.tv_description, TimeUtils.returnTime(fileItem.getLastModifiedTime()));
                CheckBox checkBox = helper.getView(R.id.checkBox);

                String path = ((FileItem) item).getPath();
                if (CheckStateManager.getInstance().get(path) == null) {        //第一次创建
                    CheckStateManager.getInstance().put(path, false);
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(CheckStateManager.getInstance().get(path));
                }
                helper.itemView.setOnClickListener(view -> {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        CheckStateManager.getInstance().put(path, false);
                    } else {
                        checkBox.setChecked(true);
                        CheckStateManager.getInstance().put(path, true);
                    }
                    if(listener != null)
                        listener.onFragmentInteraction(null);
                });

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
            case TYPE_APPLICATION_INFO:
                AppInfo appInfo = (AppInfoItem) item;
                helper.setText(R.id.tv_name, appInfo.getAppName())
                        .setText(R.id.tv_size, Tool.convertToSize(appInfo.getSize()))
                        .setText(R.id.tv_description, TimeUtils.returnTime(appInfo.getLastModified()))
                        .setImageDrawable(R.id.img_icon, appInfo.getIcon());
                checkBox = helper.getView(R.id.checkBox);

                path = ((AppInfo) item).getPath();
                if (CheckStateManager.getInstance().get(path) == null) {        //第一次创建
                    CheckStateManager.getInstance().put(path, false);
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(CheckStateManager.getInstance().get(path));
                }
                helper.itemView.setOnClickListener(view -> {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                        CheckStateManager.getInstance().put(path, false);
                    } else {
                        checkBox.setChecked(true);
                        CheckStateManager.getInstance().put(path, true);
                    }
                    if(listener != null)
                        listener.onFragmentInteraction(null);
                });

                break;
        }
    }
}
