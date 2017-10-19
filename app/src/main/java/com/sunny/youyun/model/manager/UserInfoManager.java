package com.sunny.youyun.model.manager;

import com.orhanobut.logger.Logger;
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
        List<User> list = DataSupport.where("isLocalUser = ?", String.valueOf(User.LOCAL_USER_TAG))
                .find(User.class);
        if (list == null || list.size() == 0) {
            return;
        }
        User user = list.get(0);
        Logger.i("找到一条User信息：" + user);
        getInstance().user.setUserInfo(user);
    }

    public void setUserInfo(User userInfo) {
        this.user.setUserInfo(userInfo);
        this.user.setLocalUser(User.LOCAL_USER_TAG);
        //单例化保存
        boolean result = this.user.saveOrUpdate("isLocalUser = ?", String.valueOf(User.LOCAL_USER_TAG));
    }

    /**
     * 更新用户头像
     *
     * @param avatar
     */
    public UserInfoManager updateAvatar(String avatar) {
        this.user.setAvatar(avatar);
        this.user.saveOrUpdate("isLocalUser = ?", String.valueOf(User.LOCAL_USER_TAG));
        return this;
    }

    public UserInfoManager updateNickname(String nickname) {
        this.user.setUsername(nickname);
        this.user.saveOrUpdate("isLocalUser = ?", String.valueOf(User.LOCAL_USER_TAG));
        return this;
    }

    public UserInfoManager updateSignature(String signature) {
        this.user.setSignature(signature);
        this.user.saveOrUpdate("isLocalUser = ?", String.valueOf(User.LOCAL_USER_TAG));
        return this;
    }

    public UserInfoManager updateSex(int sex) {
        this.user.setSex(sex);
        this.user.saveOrUpdate("isLocalUser = ?", String.valueOf(User.LOCAL_USER_TAG));
        return this;
    }

    public int getUserId() {
        return INSTANCE.getUserInfo().getId();
    }

    public void clear() {
        int num = DataSupport.deleteAll("User",
                "isLocalUser=" + User.LOCAL_USER_TAG);
        Logger.i("delete user: " + num);
        user = new User.Builder().build();
    }

    public User getUserInfo() {
        return user;
    }
}
