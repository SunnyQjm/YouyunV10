package com.sunny.youyun.model.manager;

import com.sunny.youyun.model.User;

/**
 * Created by Sunny on 2017/8/27 0027.
 */

public enum  UserInfoManager {
    INSTANCE;
    public static UserInfoManager getInstance(){
        return INSTANCE;
    }

    private User user = new User.Builder().build();

    public void setUserInfo(User userInfo){
        this.user.setUserInfo(userInfo);
    }

    public User getUserInfo(){
        return user;
    }
}
