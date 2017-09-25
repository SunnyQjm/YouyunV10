package com.sunny.youyun.activity.person_file_manager_index.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_file_manager_index.config.ItemTypeConfig;
import com.sunny.youyun.activity.person_file_manager_index.item.FileItem;
import com.sunny.youyun.base.adapter.BaseMultiItemQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.FileTypeUtil;
import com.sunny.youyun.utils.GlideOptions;
import com.sunny.youyun.utils.TimeUtils;
import com.sunny.youyun.utils.Tool;

import java.util.List;

/**
 * Created by Sunny on 2017/8/5 0005.
 */

public class FileAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public FileAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(ItemTypeConfig.TYPE_FILE_INFO, R.layout.file_info_item);
        addItemType(ItemTypeConfig.TYPE_DIRECT_INFO, R.layout.file_info_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        helper.getView(R.id.checkBox).setVisibility(View.INVISIBLE);
        switch (helper.getItemViewType()) {
            case ItemTypeConfig.TYPE_FILE_INFO:
                FileItem fileItem = (FileItem) item;
                InternetFile file = fileItem.getFile();
                helper.setText(R.id.tv_name, file.getName())
                        .setText(R.id.tv_size, Tool.convertToSize(file.getSize()))
                        .setText(R.id.tv_description, TimeUtils.returnTime(file.getCreateTime()));
                int resId = FileTypeUtil.getIconByFileNameWithoutVideoPhoto(fileItem.getPathName());
                if (resId == -1) {
                    Glide.with(mContext)
                            .load(file.getPath())
                            .apply(GlideOptions
                                    .getInstance().getRequestOptions())
                            .transition(GlideOptions
                                    .getInstance().getCrossFadeDrawableTransitionOptions())
                            .into((ImageView) helper.getView(R.id.img_icon));
                } else {
                    Glide.with(mContext)
                            .load(resId)
                            .apply(GlideOptions
                                    .getInstance().getRequestOptions())
                            .into((ImageView) helper.getView(R.id.img_icon));
                }
                break;
            case ItemTypeConfig.TYPE_DIRECT_INFO:
                FileItem directItem = (FileItem) item;
                helper.setText(R.id.tv_name, directItem.getPathName())
                        .setText(R.id.tv_description, TimeUtils.returnTime(directItem.getCreateTime()));
                Glide.with(mContext)
                        .load(FileTypeUtil.getDirect())
                        .apply(GlideOptions
                                .getInstance().getRequestOptions())
                        .into((ImageView) helper.getView(R.id.img_icon));
                break;
        }
    }
}
