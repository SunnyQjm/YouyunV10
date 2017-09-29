package com.sunny.youyun.model.data_item;

import com.sunny.youyun.model.User;

/**
 * Created by Sunny on 2017/9/29 0029.
 */

public class ConcernItem {

    /**
     * myId : 1010
     * otherId : 1002
     * user : {"username":"username2","email":"email2","sex":0,"phone":"18340857283","avatar":"http://q.qlogo.cn/qqapp/1105716704/9F0208D3381DA5DCBAD56380900972B2/100","score":0,"id":1002,"createTime":1506601185234,"updateTime":1506601185234}
     * id : 1003
     * createTime : 1506688150489
     * updateTime : 1506688150489
     */

    private final int myId;
    private final int otherId;
    private final User user;
    private final int id;
    private final long createTime;
    private final long updateTime;

    private ConcernItem(Builder builder) {
        myId = builder.myId;
        otherId = builder.otherId;
        user = builder.user;
        id = builder.id;
        createTime = builder.createTime;
        updateTime = builder.updateTime;
    }


    public int getMyId() {
        return myId;
    }

    public int getOtherId() {
        return otherId;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public static final class Builder {
        private int myId;
        private int otherId;
        private User user;
        private int id;
        private long createTime;
        private long updateTime;

        public Builder() {
        }

        public Builder myId(int val) {
            myId = val;
            return this;
        }

        public Builder otherId(int val) {
            otherId = val;
            return this;
        }

        public Builder user(User val) {
            user = val;
            return this;
        }

        public Builder id(int val) {
            id = val;
            return this;
        }

        public Builder createTime(long val) {
            createTime = val;
            return this;
        }

        public Builder updateTime(long val) {
            updateTime = val;
            return this;
        }

        public ConcernItem build() {
            return new ConcernItem(this);
        }
    }
}
