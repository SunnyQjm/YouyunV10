package com.sunny.youyun.model.manager;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.fragment.main.message_fragment.item.PrivateLetterItem;
import com.sunny.youyun.model.User;
import com.sunny.youyun.model.event.JPushEvent;
import com.sunny.youyun.utils.bus.MessageEventBus;

import org.litepal.crud.DataSupport;

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

    public void init(int userId) {
        //按更新时间升序查出所有的私信记录
        List<PrivateLetterItem> privateLetterItems = DataSupport.where("ownerId = ?", String.valueOf(userId))
                .order("updateTime asc")
                .find(PrivateLetterItem.class);
        for (PrivateLetterItem p :
                privateLetterItems) {
            User user = null;
            if (p.getUser() == null) {
                List<User> list = DataSupport.where("userId=?",
                        String.valueOf(p.getTargetId())).find(User.class);
                if (list.size() == 0) {
                    System.out.println("查询到0条记录");
                    continue;
                }
                user = list.get(0);
                if (user == null)
                    continue;
                p.setUser(user);
            }
            p.setUser(user);
            put(p.getUser().getId(), p);
        }
        JPushEvent jPushEvent = new JPushEvent.Builder()
                .content("")
                .title("")
                .type(JPushEvent.JUST_NOTIFY)
                .build();
        //推送一个Event
        MessageEventBus.getInstance()
                .post(jPushEvent);
    }
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

        if (message.getUser() == null)
            return;
        boolean b  = message.getUser().saveOrUpdate("userid=?", String.valueOf(userId));
        System.out.println("update message user success: " + b);
        //异步更新数据库
        message.saveOrUpdate("ownerId = ? and targetId = ?",
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
        System.out.println("headerCount: " + headCount);
        System.out.println("begin remove: " + mList.size());
        while (mList.size() > headCount){
            mList.remove(headCount);
        }
        System.out.println("remove success: " + mList.size());
    }


    private void positionChange(int count) {
        //如果这边添加，位置会后挪一位
        for (int i = 0; i < messagePosition.size(); i++) {
            int userId = messagePosition.keyAt(i);
            messagePosition.put(userId, messagePosition.get(userId) + count);
        }
    }

    private void positionChangeFromMessage(int startPosition) {
        for (int i = startPosition; i < mList.size(); i++) {
            MultiItemEntity multiItemEntity = mList.get(i);
            if (multiItemEntity instanceof PrivateLetterItem) {
                PrivateLetterItem p = (PrivateLetterItem) multiItemEntity;
                messagePosition.put(p.getUser().getId(), i);
            }
        }
    }
}
