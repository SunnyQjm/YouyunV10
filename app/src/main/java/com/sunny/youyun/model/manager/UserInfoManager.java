package com.sunny.youyun.model.manager;

import com.sunny.youyun.model.User;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Sunny on 2017/8/27 0027.
 */

public enum UserInfoManager {
    INSTANCE;

    public static UserInfoManager getInstance() {
        return INSTANCE;
    }

    private User user = new User.Builder().build();

    public static void init() {
        List<User> list = DataSupport.where("INSTANCE = ?", String.valueOf(User.INSTANCE_TAG))
                .find(User.class);
        if(list == null || list.size() == 0)
            return;
        User user = list.get(0);
        getInstance().user.setUserInfo(user);
    }

    public void setUserInfo(User userInfo) {
        this.user.setUserInfo(userInfo);
        //单例化保存
        this.user.saveOrUpdate("INSTANCE = ?", String.valueOf(User.INSTANCE_TAG));
    }

    /**
     * 更新用户头像
     * @param avatar
     */
    public void updateAvatar(String avatar){
        this.user.setAvatar(avatar);
        this.user.saveOrUpdate("INSTANCE = ?", String.valueOf(User.INSTANCE_TAG));
    }

    public void clear(){
        user.delete();
        user = new User.Builder().build();
    }

    public User getUserInfo() {
        return user;
    }
}
