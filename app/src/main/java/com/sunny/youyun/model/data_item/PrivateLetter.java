package com.sunny.youyun.model.data_item;

/**
 * 私信Item
 * Created by Sunny on 2017/10/7 0007.
 */

public class PrivateLetter {

    /**
     * username : username4
     * email : email4
     * sex : 0
     * phone : 18340857285
     * avatar : http://q.qlogo.cn/qqapp/1105716704/9F0208D3381DA5DCBAD56380900972B2/100
     * score : 10
     * message : email4
     * id : 1004
     * createTime : 1507345929956
     * updateTime : 1507347019259
     */

    private String username;
    private String email;
    private int sex;
    private String phone;
    private String avatar;
    private int score;
    private Message message;
    private int id;
    private long createTime;
    private long updateTime;

    protected PrivateLetter(Builder builder) {
        username = builder.username;
        email = builder.email;
        sex = builder.sex;
        phone = builder.phone;
        avatar = builder.avatar;
        score = builder.score;
        message = builder.message;
        id = builder.id;
        createTime = builder.createTime;
        updateTime = builder.updateTime;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getScore() {
        return score;
    }

    public Message getMessage() {
        return message;
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
        private String username;
        private String email;
        private int sex;
        private String phone;
        private String avatar;
        private int score;
        private Message message;
        private int id;
        private long createTime;
        private long updateTime;

        public Builder() {
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder sex(int val) {
            sex = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder avatar(String val) {
            avatar = val;
            return this;
        }

        public Builder score(int val) {
            score = val;
            return this;
        }

        public Builder message(Message val) {
            message = val;
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

        public PrivateLetter build() {
            return new PrivateLetter(this);
        }
    }
}
