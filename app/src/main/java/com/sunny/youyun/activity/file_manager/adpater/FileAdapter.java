package com.sunny.youyun.activity.file_manager.adpater;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.config.ItemTypeConfig;
import com.sunny.youyun.activity.file_manager.item.DirectItem;
import com.sunny.youyun.activity.file_manager.item.FileItem;
import com.sunny.youyun.activity.file_manager.manager.CheckStateManager;
import com.sunny.youyun.base.adapter.BaseMultiItemQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.utils.FileTypeUtil;
import com.sunny.youyun.utils.GlideOptions;
import com.sunny.youyun.utils.TimeUtils;
import com.sunny.youyun.utils.Tool;

import java.util.List;

/**
 * Created by Sunny on 2017/8/5 0005.
 */

public class FileAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private final MVPBaseFragment.OnFragmentInteractionListener listener;
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     * @param mListener
     */
    public FileAdapter(List<MultiItemEntity> data, MVPBaseFragment.OnFragmentInteractionListener mListener) {
        super(data);
        this.listener = mListener;
        addItemType(ItemTypeConfig.TYPE_FILE_INFO, R.layout.file_info_item);
        addItemType(ItemTypeConfig.TYPE_DIRECT_INFO, R.layout.file_info_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case ItemTypeConfig.TYPE_FILE_INFO:
                FileItem fileItem = (FileItem) item;
                helper.setText(R.id.tv_name, fileItem.getName())
                        .setText(R.id.tv_size, Tool.convertToSize(fileItem.getSize()))
                        .setText(R.id.tv_time, TimeUtils.returnTime(fileItem.getLastModifiedTime()));
                CheckBox checkBox = helper.getView(R.id.checkBox);

                String path = fileItem.getPath();
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
                int resId = FileTypeUtil.getIconByFileNameWithoutVideoPhoto(fileItem.getName());
                if (resId == -1) {
                    Glide.with(mContext)
                            .load(fileItem.getPath())
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
                DirectItem directItem = (DirectItem) item;
                helper.setText(R.id.tv_name, directItem.getName())
                        .setText(R.id.tv_size, Tool.convertToSize(directItem.getSize()))
                        .setText(R.id.tv_time, TimeUtils.returnTime(directItem.getLastModifiedTime()));
                helper.getView(R.id.checkBox).setVisibility(View.INVISIBLE);
                Glide.with(mContext)
                        .load(FileTypeUtil.getDirect())
                        .apply(GlideOptions
                                .getInstance().getRequestOptions())
                        .into((ImageView) helper.getView(R.id.img_icon));
                break;
        }
    }
}
