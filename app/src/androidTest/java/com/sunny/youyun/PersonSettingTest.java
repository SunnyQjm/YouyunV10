package com.sunny.youyun;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sunny.youyun.activity.login.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * 用户设置 testing
 * Created by Sunny on 2017/10/21 0021.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PersonSettingTest {
    private String phone;
    private String password;
    private IdlingResource idlingResource;
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<LoginActivity>(
            LoginActivity.class
    );

    @Before
    public void init() {
        phone = "18340857280";
        password = "123456";
        idlingResource = mActivityRule.getActivity().getCountingIdlingResource();
        IdlingRegistry.getInstance()
                .register(idlingResource);
    }

    @Test
    public void personSetting() {
        //输入用户名和密码
        onView(withId(R.id.et_username))
                .perform(typeText(phone), closeSoftKeyboard());
        onView(withId(R.id.et_password))
                .perform(typeText(password), closeSoftKeyboard());

        //点击登录按钮(会触发登录)
        onView(withId(R.id.btn_login))
                .perform(click());

        //成功登录以后
        onView(withId(R.id.btn_mine))
                .perform(click());
        onView(withId(R.id.img_edit))
                .perform(click());

        //修改用户名
        onView(withId(R.id.li_change_nickname))
                .perform(click());
        onView(withId(R.id.editText))
                .perform(clearText())
                .perform(typeText("Sunny"));
        onView(withId(R.id.tv_sure))
                .perform(click());

        //修改个签
        onView(withId(R.id.person_setting_signature_et))
                .perform(clearText())
                .perform(replaceText("什么都没有留下~~"));
        onView(withId(R.id.person_setting_signature_img_sure))
                .perform(click());

        //修改头像
        onView(withId(R.id.li_change_avatar))
                .perform(click());
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.bar_right_icon))
                .perform(click());


    }

}
