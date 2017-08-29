package com.sunny.youyun.wifidirect.activity.single_trans.adapter;

import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.utils.FileTypeUtil;
import com.sunny.youyun.utils.GlideOptions;
import com.sunny.youyun.utils.TimeUtils;
import com.sunny.youyun.utils.Tool;
import com.sunny.youyun.wifidirect.model.TransLocalFile;

import java.util.List;
import java.util.Objects;

/**
 * Created by Sunny on 2017/8/7 0007.
 */

public class FileRecordAdapter extends BaseQuickAdapter<TransLocalFile, BaseViewHolder> {

    private final static byte[] tag = new byte[0];

    public FileRecordAdapter(@LayoutRes int layoutResId, @Nullable List<TransLocalFile> data) {
        super(layoutResId, data);
    }

    @Nullable
    @Override
    public TransLocalFile getItem(@IntRange(from = 0) int position) {
        return super.getItem(mData.size() - 1 - position);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransLocalFile item) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Objects.equals(helper.itemView.getTag(), tag)) {
                updateWithPayloads(helper, item);
                Logger.i("upload with payloads");
            }
            else
                update(helper, item);
        } else {
            if(tag.equals(helper.itemView.getTag()))
                updateWithPayloads(helper, item);
            else
                update(helper, item);
        }

        update(helper, item);
    }

    private void updateWithPayloads(BaseViewHolder helper, TransLocalFile item) {
        Logger.i("onBindViewHolder with payloads");
        if (item.isDone()) {
            helper.setText(R.id.tv_time, TimeUtils.returnTime(item.getCreateTime()))
                    .setText(R.id.tv_size, Tool.convertToSize(item.getSize()));
            helper.setVisible(R.id.progressBar, false);
            helper.setVisible(R.id.tv_rate, false);
            helper.setVisible(R.id.tv_time, true);
            helper.setVisible(R.id.tv_size, true);
        } else {
            helper.setProgress(R.id.progressBar, item.getProgress())
                    .setText(R.id.tv_rate, item.getRate());
            helper.setVisible(R.id.progressBar, true);
            helper.setVisible(R.id.tv_rate, true);
            helper.setVisible(R.id.tv_time, false);
            helper.setVisible(R.id.tv_size, false);
        }
    }

    private void update(BaseViewHolder helper, TransLocalFile item) {
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
        if (result == -1) {
            System.out.println(item.getPath());
            Glide.with(mContext)
                    .load(item.getPath())
                    .apply(GlideOptions.getInstance().getRequestOptions())
                    .transition(GlideOptions.getInstance().getCrossFadeDrawableTransitionOptions())
                    .into(((ImageView) helper.getView(R.id.img_icon)));
        } else {
            Glide.with(mContext)
                    .load(result)
                    .into(((ImageView) helper.getView(R.id.img_icon)));
        }
    }
}
