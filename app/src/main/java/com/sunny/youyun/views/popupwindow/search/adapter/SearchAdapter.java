package com.sunny.youyun.views.popupwindow.search.adapter;

import android.widget.ImageView;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseMultiItemQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.finding_fragment.config.SearchItemType;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.User;
import com.sunny.youyun.model.YouyunDefaultInfoManager;
import com.sunny.youyun.utils.GlideUtils;

import java.util.List;

/**
 * Created by Sunny on 2017/10/4 0004.
 */

public class SearchAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SearchAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(SearchItemType.TYPE_FILE_TAG, R.layout.tag);
        addItemType(SearchItemType.TYPE_USER_TAG, R.layout.tag);
        addItemType(SearchItemType.TYPE_USER, R.layout.search_item);
        addItemType(SearchItemType.TYPE_FILE, R.layout.search_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case SearchItemType.TYPE_FILE:
                if (item instanceof InternetFile) {
                    InternetFile internetFile = (InternetFile) item;
                    helper.setText(R.id.tv_name, internetFile.getName())
                            .setText(R.id.tv_description, YouyunDefaultInfoManager
                                    .getDefaultFileDescription(internetFile.getDescription()));
                    GlideUtils.setImage(mContext, ((ImageView) helper.getView(R.id.img_icon)),
                            internetFile);
                }
                break;
            case SearchItemType.TYPE_FILE_TAG:
                helper.setText(R.id.textView, mContext.getString(R.string.file));
                break;
            case SearchItemType.TYPE_USER:
                if (item instanceof User) {
                    User user = (User) item;
                    helper.setText(R.id.tv_name, user.getUsername())
                            .setText(R.id.tv_description, YouyunDefaultInfoManager
                                    .getDefaultUserSignature(user.getSignature()));
                    GlideUtils.load(mContext, ((ImageView) helper.getView(R.id.img_icon)),
                            user.getAvatar());
                }
                break;
            case SearchItemType.TYPE_USER_TAG:
                helper.setText(R.id.textView, mContext.getString(R.string.user));
                break;
        }
    }
}
