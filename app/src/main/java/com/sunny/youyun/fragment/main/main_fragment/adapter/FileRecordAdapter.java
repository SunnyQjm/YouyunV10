package com.sunny.youyun.fragment.main.main_fragment.adapter;

import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.manager.CheckStateManager;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.FileTypeUtil;
import com.sunny.youyun.utils.GlideOptions;
import com.sunny.youyun.utils.TimeUtils;
import com.sunny.youyun.utils.Tool;

import java.util.List;

import static com.sunny.youyun.model.InternetFile.Status;

/**
 * Created by Sunny on 2017/8/7 0007.
 */

public class FileRecordAdapter extends BaseQuickAdapter<InternetFile, BaseViewHolder> {

    private Mode mode = Mode.NORMAL;


    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public enum Mode {
        NORMAL, SELECT
    }

    public FileRecordAdapter(@LayoutRes int layoutResId, @Nullable List<InternetFile> data) {
        super(layoutResId, data);
        this.openLoadAnimation(SLIDEIN_RIGHT);
        this.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
    }

    @Nullable
    @Override
    public InternetFile getItem(@IntRange(from = 0) int position) {
        return super.getItem(mData.size() - 1 - position);
    }

    @Override
    protected void convert(BaseViewHolder helper, InternetFile item) {
        update(helper, item);
    }

    private void update(BaseViewHolder helper, InternetFile item) {
        if (mode == Mode.SELECT) {
            helper.setVisible(R.id.img_arrow, false);
            helper.setVisible(R.id.checkBox, true);
        } else if (mode == Mode.NORMAL) {
            helper.setVisible(R.id.img_arrow, true);
            helper.setVisible(R.id.checkBox, false);
        }
        helper.setChecked(R.id.checkBox, CheckStateManager.getInstance().get(item.getPath()));
        switch (item.getStatus()) {
            case Status.FINISH:
                helper.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_time, TimeUtils.returnTime(item.getCreateTime()))
                        .setText(R.id.tv_size, Tool.convertToSize(item.getSize()));
                progressStyle(helper, Status.FINISH);
                break;
            case Status.DOWNLOADING:
                helper.setText(R.id.tv_name, item.getName())
                        .setProgress(R.id.progressBar, item.getProgress())
                        .setText(R.id.tv_rate, item.getRate());
                progressStyle(helper, Status.DOWNLOADING);
                break;
            case Status.ERROR:
                helper.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_time, TimeUtils.returnTime(item.getCreateTime()))
                        .setText(R.id.tv_size, mContext.getString(R.string.error));
                progressStyle(helper, Status.ERROR);
                break;
            case Status.CANCEL:
            case Status.PAUSE:
                helper.setText(R.id.tv_name, item.getName())
                        .setProgress(R.id.progressBar, item.getProgress())
                        .setText(R.id.tv_rate, item.getRate());
                progressStyle(helper, Status.PAUSE);
                break;
        }

        //显示文件icon
        int result = FileTypeUtil.getIconByFileNameWithoutVideoPhoto(item.getName());
        if (result == -1 && item.isDone()) {
            Glide.with(mContext)
                    .load(item.getPath())
                    .apply(GlideOptions.getInstance().getRequestOptions())
                    .transition(GlideOptions.getInstance().getCrossFadeDrawableTransitionOptions())
                    .into(((ImageView) helper.getView(R.id.img_icon)));
            return;
        }
//        else if (stringResult == FileTypeUtil.getApk() && item.isDone() && mContext.getClass() == Activity.class) {
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
     * 该方法改变item的样式
     * if isProgressStyle = true, than display item with progress bar
     *
     * @param helper
     */
    private void progressStyle(BaseViewHolder helper, String status) {
        CheckBox checkBox = (CheckBox)helper.getView(R.id.checkBox);
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
                if (status.equals(Status.ERROR))
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
