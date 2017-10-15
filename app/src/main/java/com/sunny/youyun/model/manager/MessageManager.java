package com.sunny.youyun.model.manager;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.sunny.youyun.model.data_item.Message;

/**
 * 消息未读计数器
 * Created by Sunny on 2017/10/7 0007.
 */

public enum  MessageManager {
    INSTANCE;
    public static MessageManager getInstance(){
        return INSTANCE;
    }

    private final SparseIntArray redBubbleCounts = new SparseIntArray();
    private final SparseArray<Message> lastMessageManager = new SparseArray<>();

    ////////////////////////////////////////////////////////////////
    ///////////未读消息数量管理
    ///////////////////////////////////////////////////////////////

    public void addCount(int userId, int count){
        redBubbleCounts.put(userId, redBubbleCounts.get(userId) + count);
    }
    public void addCount(int userId){
        addCount(userId, 1);
    }

    public void minusCount(int userId){
        minusCount(userId, 1);
    }
    public void minusCount(int userId, int count){
        if(redBubbleCounts.get(userId) >= count){
            redBubbleCounts.put(userId, redBubbleCounts.get(userId) - count);
        }
    }

    public void clearCount(int userId){
        redBubbleCounts.delete(userId);
    }

    public void clearAll(){
        redBubbleCounts.clear();
        lastMessageManager.clear();
    }

    public int getCount(int userId){
        return redBubbleCounts.get(userId);
    }

    public int getTotalCount(){
        int total = 0;
        for (int i = 0; i < redBubbleCounts.size(); i++) {
            total += redBubbleCounts.valueAt(i);
        }
        return total;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////最新的一条未读消息管理
    ///////////////////////////////////////////////////////////////////////////

    public void put(int userId, Message message){
        lastMessageManager.put(userId, message);
    }

    public Message getMessage(int userId){
        return lastMessageManager.get(userId);
    }

    public void clearMessage(){
        lastMessageManager.clear();
    }
}
