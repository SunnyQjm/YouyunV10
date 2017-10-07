package com.sunny.youyun.fragment.main.message_fragment.adapter;

import com.sunny.youyun.R;
import com.sunny.youyun.base.adapter.BaseMultiItemQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.message_fragment.item.HeaderItem;
import com.sunny.youyun.fragment.main.message_fragment.item.PrivateLetterItem;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.TimeUtils;
import com.sunny.youyun.views.LineMenuItem;

import java.util.List;

/**
 * Created by Sunny on 2017/9/25 0025.
 */

public class MessageAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MessageAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TypeConfig.TYPE_HEADER, R.layout.message_header_item);
        addItemType(TypeConfig.TYPE_MESSAGE, R.layout.message_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case TypeConfig.TYPE_HEADER:    //头布局填充
                HeaderItem headerItem = (HeaderItem) item;
                LineMenuItem lineMenuItem = helper.getView(R.id.message_header);
                lineMenuItem.setTitle(headerItem.getText());
                lineMenuItem.setLeftIcon(headerItem.getRes());
                break;
            case TypeConfig.TYPE_MESSAGE:   //消息内容填充
                PrivateLetterItem messageItem = (PrivateLetterItem) item;
                helper.setText(R.id.tv_name, messageItem.getUsername())
                        .setText(R.id.tv_description, messageItem.getMessage().getContent())
                        .setText(R.id.tv_date, TimeUtils.returnTime_ymd(messageItem.getCreateTime()));
                GlideUtils.load(mContext, helper.getView(R.id.img_icon), messageItem.getAvatar());
                break;
        }
    }
}
