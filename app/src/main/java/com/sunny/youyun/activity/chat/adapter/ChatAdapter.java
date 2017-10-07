package com.sunny.youyun.activity.chat.adapter;

import com.sunny.youyun.R;
import com.sunny.youyun.activity.chat.config.ChatConfig;
import com.sunny.youyun.activity.chat.item.DateItem;
import com.sunny.youyun.base.adapter.BaseMultiItemQuickAdapter;
import com.sunny.youyun.base.adapter.BaseViewHolder;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.utils.TimeUtils;

import java.util.List;

/**
 * Created by Sunny on 2017/10/6 0006.
 */

public class ChatAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(ChatConfig.MESSAGE_ITEM_TYPE_OTHER, R.layout.chat_item_left);
        addItemType(ChatConfig.MESSAGE_ITEM_TYPE_SELF, R.layout.chat_item_right);
        addItemType(ChatConfig.MESSAGE_ITEM_TYPE_DATE, R.layout.chat_item_date);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case ChatConfig.MESSAGE_ITEM_TYPE_OTHER:
            case ChatConfig.MESSAGE_ITEM_TYPE_SELF:

                break;
            case ChatConfig.MESSAGE_ITEM_TYPE_DATE:
                if (item instanceof DateItem) {
                    DateItem dateItem = (DateItem) item;
                    helper.setText(R.id.tv_date,
                            TimeUtils.returnTime_y4md_line_divide(dateItem.getDate()));
                }
                break;
        }
    }
}
