package com.sunny.youyun.model.manager;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.message_fragment.item.PrivateLetterItem;
import com.sunny.youyun.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息未读计数器
 * Created by Sunny on 2017/10/7 0007.
 */

public enum MessageManager {
    INSTANCE;

    public static MessageManager getInstance() {
        return INSTANCE;
    }

    private final SparseIntArray redBubbleCounts = new SparseIntArray();
    private final SparseArray<PrivateLetterItem> lastMessageManager = new SparseArray<>();
    private final SparseIntArray messagePosition = new SparseIntArray();
    private final List<MultiItemEntity> mList = new ArrayList<>();
    private int headCount = 0;

    ////////////////////////////////////////////////////////////////
    ///////////未读消息数量管理
    ///////////////////////////////////////////////////////////////

    public void addCount(int userId, int count) {
        redBubbleCounts.put(userId, redBubbleCounts.get(userId) + count);
    }

    public void addCount(int userId) {
        addCount(userId, 1);
    }

    public void minusCount(int userId) {
        minusCount(userId, 1);
    }

    public void minusCount(int userId, int count) {
        if (redBubbleCounts.get(userId) >= count) {
            redBubbleCounts.put(userId, redBubbleCounts.get(userId) - count);
        }
    }

    public void clearCount(int userId) {
        redBubbleCounts.delete(userId);
    }

    public void clearAll() {
        redBubbleCounts.clear();
        lastMessageManager.clear();
    }

    public int getCount(int userId) {
        return redBubbleCounts.get(userId);
    }

    public int getTotalCount() {
        int total = 0;
        for (int i = 0; i < redBubbleCounts.size(); i++) {
            total += redBubbleCounts.valueAt(i);
        }
        return total;
    }


    /**
     * 添加头部
     *
     * @param multiItemEntity
     */
    public synchronized void addHeader(MultiItemEntity multiItemEntity) {
        mList.add(0, multiItemEntity);
        headCount++;
        positionChange(1);
    }
    ////////////////////////////////////////////////////////////////////////////
    ////////////最新的一条未读消息管理
    ///////////////////////////////////////////////////////////////////////////

    public void put(int userId, PrivateLetterItem message) {

        //异步更新数据库
        message.saveOrUpdateAsync("ownerId = ? and targetId",
                String.valueOf(UserInfoManager.getInstance().getUserId()), String.valueOf(userId));
        if (messagePosition.get(userId) == 0) {       //如果之前没加就往后添加
            mList.add(headCount, message);
            positionChange(1);
            messagePosition.put(userId, headCount);
        } else {
            MultiItemEntity multiItemEntity = mList.get(messagePosition.get(userId));
            if (multiItemEntity == null || !(multiItemEntity instanceof PrivateLetterItem))
                return;
            PrivateLetterItem m = (PrivateLetterItem) multiItemEntity;
            System.out.println("put: " + userId);
            System.out.println(GsonUtil.bean2Json(m));
            if (message.getUpdateTime() > m.getUpdateTime()) {
                int position = messagePosition.get(userId);
                mList.remove(position);
                positionChangeFromMessage(position);
                mList.add(headCount, message);
                positionChange(1);
                messagePosition.put(userId, headCount);
            }
        }
    }

    public PrivateLetterItem getMessage(int userId) {
        return (PrivateLetterItem) mList
                .get(messagePosition.get(userId) == 0 ? 0 : messagePosition.get(userId) - 1);
    }

    public List<MultiItemEntity> getMessages() {
        return mList;
    }

    public void clearMessage() {
        lastMessageManager.clear();
        messagePosition.clear();
        mList.clear();
    }


    private void positionChange(int count){
        //如果这边添加，位置会后挪一位
        for (int i = 0; i < messagePosition.size(); i++) {
            int userId = messagePosition.keyAt(i);
            messagePosition.put(userId, messagePosition.get(userId) + count);
        }
    }

    private void positionChangeFromMessage(int startPosition){
        for (int i = startPosition; i < mList.size(); i++) {
            MultiItemEntity multiItemEntity = mList.get(i);
            if(multiItemEntity instanceof PrivateLetterItem){
                PrivateLetterItem p = (PrivateLetterItem) multiItemEntity;
                messagePosition.put(p.getUser().getId(), i);
            }
        }
    }
}
