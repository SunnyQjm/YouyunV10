package com.sunny.youyun.model;

import org.litepal.crud.DataSupport;

/**
 * Created by Sunny on 2017/6/6 0006.
 */

public class User extends DataSupport{

    /**
     * username : username1
     * email : email1
     * sex : 1
     * phone : phone1
     * avatar : http://123.206.80.54:8080/avatar/WiTe-a.jpg
     * score : 10
     * id : 1001
     * createTime : 1496584061628
     * updateTime : 1496630108830
     */

    private String username;
    private String email;
    private int sex;
    private String phone;
    private String avatar;
    private int score;
    private int id;
    private long createTime;
    private long updateTime;
    public static final int INSTANCE_TAG = 0;
    private final int INSTANCE = INSTANCE_TAG;

    private User(Builder builder) {
        setUsername(builder.username);
        setEmail(builder.email);
        setSex(builder.sex);
        setPhone(builder.phone);
        setAvatar(builder.avatar);
        setScore(builder.score);
        setId(builder.id);
        setCreateTime(builder.createTime);
        setUpdateTime(builder.updateTime);
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", sex=" + sex +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", score=" + score +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setUserInfo(User user){
        this.username = user.username;
        this.email = user.email;
        this.sex = user.sex;
        this.phone = user.phone;
        this.avatar = user.avatar;
        this.score = user.score;
        this.id = user.id;
        this.createTime = user.createTime;
        this.updateTime = user.updateTime;
    }

    public static final class Builder {
        private String username;
        private String email;
        private int sex;
        private String phone;
        private String avatar;
        private int score;
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

        public User build() {
            return new User(this);
        }
    }
}
