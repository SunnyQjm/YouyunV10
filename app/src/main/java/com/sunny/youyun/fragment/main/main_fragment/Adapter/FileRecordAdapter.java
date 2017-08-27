package com.sunny.youyun.fragment.main.main_fragment.Adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.R;
import com.sunny.youyun.base.BaseQuickAdapter;
import com.sunny.youyun.base.BaseViewHolder;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.FileTypeUtil;
import com.sunny.youyun.utils.GlideOptions;
import com.sunny.youyun.utils.TimeUtils;
import com.sunny.youyun.utils.Tool;

import java.util.List;

/**
 * Created by Sunny on 2017/8/7 0007.
 */

public class FileRecordAdapter extends BaseQuickAdapter<InternetFile, BaseViewHolder> {

    private final static byte[] tag = new byte[0];
    @DrawableRes
    private int displayIcon = R.drawable.icon_arrow;

    public void setDisplayIcon(int displayIcon) {
        this.displayIcon = displayIcon;
    }

    public FileRecordAdapter(@LayoutRes int layoutResId, @Nullable List<InternetFile> data) {
        super(layoutResId, data);
    }

    @Nullable
    @Override
    public InternetFile getItem(@IntRange(from = 0) int position) {
        return super.getItem(mData.size() - 1 - position);
    }

    @Override
    protected void convert(BaseViewHolder helper, InternetFile item) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (Objects.equals(helper.itemView.getTag(), tag)) {
//                updateWithPayloads(helper, item);
//                Logger.i("upload with payloads");
//            } else
//                update(helper, item);
//        } else {
//            if (tag.equals(helper.itemView.getTag()))
//                updateWithPayloads(helper, item);
//            else
//                update(helper, item);
//        }

        update(helper, item);
    }

    private void updateWithPayloads(BaseViewHolder helper, InternetFile item) {
        Logger.i("onBindViewHolder with payloads");
        if (item.isDone()) {
            helper.setText(R.id.tv_time, TimeUtils.returnTime(item.getCreateTime()))
                    .setText(R.id.tv_size, Tool.convertToSize(item.getSize()))
                    .setImageResource(R.id.img_arrow, displayIcon);
            helper.setVisible(R.id.progressBar, false);
            helper.setVisible(R.id.tv_rate, false);
            helper.setVisible(R.id.tv_time, true);
            helper.setVisible(R.id.tv_size, true);
        } else {
            helper.setProgress(R.id.progressBar, item.getProgress())
                    .setText(R.id.tv_rate, item.getRate())
                    .setImageResource(R.id.img_arrow, displayIcon);
            helper.setVisible(R.id.progressBar, true);
            helper.setVisible(R.id.tv_rate, true);
            helper.setVisible(R.id.tv_time, false);
            helper.setVisible(R.id.tv_size, false);
        }
    }

    private void update(BaseViewHolder helper, InternetFile item) {
        if (item.isDone()) {
            helper.setText(R.id.tv_name, item.getName())
                    .setText(R.id.tv_time, TimeUtils.returnTime(item.getCreateTime()))
                    .setText(R.id.tv_size, Tool.convertToSize(item.getSize()));
            helper.setVisible(R.id.progressBar, false);
            helper.setVisible(R.id.tv_rate, false);
            helper.setVisible(R.id.tv_time, true);
            helper.setVisible(R.id.tv_size, true);
        } else {
            helper.setText(R.id.tv_name, item.getName())
                    .setProgress(R.id.progressBar, item.getProgress())
                    .setText(R.id.tv_rate, item.getRate());
            helper.setVisible(R.id.progressBar, true);
            helper.setVisible(R.id.tv_rate, true);
            helper.setVisible(R.id.tv_time, false);
            helper.setVisible(R.id.tv_size, false);

        }

        int result = FileTypeUtil.getIconByFileNameWithoutVideoPhoto(item.getName());
        if (result == -1 && item.isDone()) {
            Glide.with(mContext)
                    .load(item.getPath())
                    .apply(GlideOptions.getInstance().getRequestOptions())
                    .transition(GlideOptions.getInstance().getCrossFadeDrawableTransitionOptions())
                    .into(((ImageView) helper.getView(R.id.img_icon)));
            return;
        }
//        else if (result == FileTypeUtil.getApk() && item.isDone() && mContext.getClass() == Activity.class) {
//            Drawable drawable = ApkInfoUtil.getIcon((Activity) mContext, item.getPath());
//            Glide.with(mContext)
//                    .load(drawable)
//                    .apply(GlideOptions.getInstance().getRequestOptions())
//                    .transition(GlideOptions.getInstance().getCrossFadeDrawableTransitionOptions())
//                    .into(((ImageView) helper.getView(R.id.img_icon)));
//            return;
//        }
        Glide.with(mContext)
                .load(FileTypeUtil.getIconIdByFileName(item.getName()))
                .into(((ImageView) helper.getView(R.id.img_icon)));

    }

    /**
     * remove item
     *
     * @param position the real view position, is reverse than data
     */
    @Override
    public void remove(int position) {
        int internalPosition = position + getHeaderLayoutCount();
        notifyItemRemoved(internalPosition);
        mData.remove(mData.size() - 1 - position);
        notifyDataSetChanged();
        notifyItemRangeChanged(internalPosition, mData.size() - internalPosition);
    }
}
