package com.sunny.youyun;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sunny.youyun.activity.main.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Sunny on 2017/10/21 0021.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginInstrumentedTest {
    private String phone;
    private String password;
    private IdlingResource idlingResource;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class
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
    public void testLogin() {
        onView(withId(R.id.btn_mine))
                .perform(click());
        onView(withId(R.id.li_setting))
                .perform(click());
        onView(withId(R.id.exit))
                .perform(click());

        onView(withId(R.id.tv_sure))
                .perform(click());
        //输入用户名和密码
        onView(withId(R.id.et_username))
                .perform(typeText(phone), closeSoftKeyboard());
        onView(withId(R.id.et_password))
                .perform(typeText(password), closeSoftKeyboard());

        //点击登录按钮(会触发登录)
        onView(withId(R.id.btn_login))
                .perform(click());
        onView(withId(R.id.img_qr_code))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

//        comment();
    }

    private void comment(){
        //跳到发现
        onView(withId(R.id.btn_find))
                .perform(click());
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
    @After
    public void after(){
        IdlingRegistry.getInstance()
                .unregister(idlingResource);
    }
}
