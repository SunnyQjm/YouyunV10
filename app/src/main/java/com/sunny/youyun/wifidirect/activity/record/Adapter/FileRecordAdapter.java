package com.sunny.youyun.wifidirect.activity.record.Adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.utils.FileTypeUtil;
import com.sunny.youyun.utils.GlideOptions;
import com.sunny.youyun.utils.TimeUtils;
import com.sunny.youyun.utils.Tool;
import com.sunny.youyun.wifidirect.model.TransLocalFile;

import java.util.List;

import static com.sunny.youyun.model.InternetFile.Status;

/**
 * Created by Sunny on 2017/8/7 0007.
 */

public class FileRecordAdapter extends BaseQuickAdapter<TransLocalFile, BaseViewHolder> {

    private final static byte[] tag = new byte[0];
    @DrawableRes
    private int displayIcon = R.drawable.icon_arrow;

    public void setDisplayIcon(int displayIcon) {
        this.displayIcon = displayIcon;
    }

    public FileRecordAdapter(@LayoutRes int layoutResId, @Nullable List<TransLocalFile> data) {
        super(layoutResId, data);
        this.openLoadAnimation(SLIDEIN_RIGHT);
        this.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
    }

    @Nullable
    @Override
    public TransLocalFile getItem(@IntRange(from = 0) int position) {
        return super.getItem(mData.size() - 1 - position);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransLocalFile item) {
        update(helper, item);
    }

    private void update(BaseViewHolder helper, TransLocalFile item) {
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_time, TimeUtils.returnTime(item.getCreateTime()))
                .setText(R.id.tv_size, Tool.convertToSize(item.getSize()));
        progressStyle(helper, Status.FINISH);

        int result = FileTypeUtil.getIconByFileNameWithoutVideoPhoto(item.getName());
        if (result == -1 && item.isDone()) {
            Glide.with(mContext)
                    .load(item.getPath())
                    .apply(GlideOptions.getInstance().getRequestOptions())
                    .transition(GlideOptions.getInstance().getCrossFadeDrawableTransitionOptions())
                    .into(((ImageView) helper.getView(R.id.img_icon)));
            return;
        }
        Glide.with(mContext)
                .load(FileTypeUtil.getIconIdByFileName(item.getName()))
                .into(((ImageView) helper.getView(R.id.img_icon)));

    }

    /**
     * 该方法改变item的样式
     * if isProgressStyle = true, than display item with progress bar
     *
     * @param helper
     */
    private void progressStyle(BaseViewHolder helper, String status) {
        switch (status) {
            case Status.DOWNLOADING:
            case Status.PAUSE:
                helper.setVisible(R.id.progressBar, true);
                helper.setVisible(R.id.tv_rate, true);
                helper.setVisible(R.id.tv_time, false);
                helper.setVisible(R.id.tv_size, false);
                if (status.equals(Status.PAUSE)) {
                    helper.setImageResource(R.id.img_arrow, R.drawable.icon_start);
                } else {
                    helper.setImageResource(R.id.img_arrow, R.drawable.icon_stop);
                }
                break;
            case Status.FINISH:
            case Status.CANCEL:
            case Status.ERROR:
                helper.setVisible(R.id.progressBar, false);
                helper.setVisible(R.id.tv_rate, false);
                helper.setVisible(R.id.tv_time, true);
                helper.setVisible(R.id.tv_size, true);
                if(status.equals(Status.ERROR))
                    helper.setImageResource(R.id.img_arrow, R.drawable.icon_erro);
                else
                    helper.setImageResource(R.id.img_arrow, R.drawable.icon_arrow);
                break;
        }
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
