package com.sunny.youyun.model.model_interface;

import com.sunny.youyun.model.User;

/**
 * Created by Sunny on 2017/6/6 0006.
 */

public class UserInterface {
    private static User instance;
    private static boolean isLogin = false;

    public static User getInstance() {
        if(instance == null)
            instance = new User.Builder().build();
        return instance;
    }

    /**
     * 用用户对象更新用户信息
     * @param user
     */
    public static void setUserInfo(User user){
        getInstance().setUserInfo(user);
    }

    public static boolean isLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        UserInterface.isLogin = isLogin;
    }
}
