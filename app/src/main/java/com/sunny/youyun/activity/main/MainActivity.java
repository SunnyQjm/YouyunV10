package com.sunny.youyun.activity.main;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.githang.statusbar.StatusBarCompat;
import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.main.adapter.MainAdapter;
import com.sunny.youyun.activity.main.config.MainActivityConfig;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.fragment.main.finding_fragment.FindingFragment;
import com.sunny.youyun.fragment.main.main_fragment.MainFragment;
import com.sunny.youyun.fragment.main.message_fragment.MessageFragment;
import com.sunny.youyun.fragment.main.mine_fragment.MineFragment;
import com.sunny.youyun.utils.MyNotifyUtil;
import com.sunny.youyun.utils.TimePickerUtils;
import com.sunny.youyun.views.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IntentRouter.MainActivity)
public class MainActivity extends MVPBaseActivity<MainPresenter> implements MainContract.View, MVPBaseFragment.OnFragmentInteractionListener {


    private static final int MAIN_PAGE_FRAGMENT = 0;
    private static final int FINDING_PAGE_FRAGMENT = 1;
    private static final int MESSAGE_PAGE_FRAGMENT = 2;
    private static final int MINE_PAGE_FRAGMENT = 3;

    @BindView(R.id.btn_main_page)
    ImageView btnMainPage;
    @BindView(R.id.btn_find)
    ImageView btnFind;
    @BindView(R.id.btn_msg)
    ImageView btnMsg;
    @BindView(R.id.btn_mine)
    ImageView btnMine;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;

    private FragmentManager fragmentManager;
    private MainFragment mainFragment;
    private FindingFragment findingFragment;
    private MessageFragment messageFragment;
    private MineFragment mineFragment;

    private List<Fragment> fragmentList = new ArrayList<>();
    private MainAdapter mainAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        MyNotifyUtil.setShowTag(MyNotifyUtil.SHOW_TAG_MAIN);
        TimePickerUtils.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyNotifyUtil.setShowTag(MyNotifyUtil.SHOW_TAG_OTHER);
        TimePickerUtils.unBind();
    }

    /**
     * activity 采用SingleTask模式
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null)
            return;
        String tag = intent.getStringExtra(MainActivityConfig.LUNCH_TAG);
        if (tag == null || tag.equals(""))
            return;
        switch (tag) {
            case MainActivityConfig.LUNCH_TAG_UPLOAD_DOWNLOAD:
                viewpager.setCurrentItem(MAIN_PAGE_FRAGMENT, false);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.blue, getTheme()));
        } else {
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.blue));
        }
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        System.out.println("initView");
        mainFragment = MainFragment.newInstance();
        findingFragment = FindingFragment.newInstance();
        messageFragment = MessageFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        fragmentList.add(mainFragment);
        fragmentList.add(findingFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(mineFragment);
        mainAdapter = new MainAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(mainAdapter);
        viewpager.setScroll(false);
        viewpager.setCurrentItem(MAIN_PAGE_FRAGMENT, false);
    }


    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void showTip(String info) {
        super.showTip(info);
    }

    @OnClick({R.id.btn_main_page, R.id.btn_find, R.id.btn_msg, R.id.btn_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main_page:
                viewpager.setCurrentItem(MAIN_PAGE_FRAGMENT, false);
                mPresenter.changeBottomImg(MAIN_PAGE_FRAGMENT);
                break;
            case R.id.btn_find:
                viewpager.setCurrentItem(FINDING_PAGE_FRAGMENT, false);
                mPresenter.changeBottomImg(FINDING_PAGE_FRAGMENT);
                break;
            case R.id.btn_msg:
                viewpager.setCurrentItem(MESSAGE_PAGE_FRAGMENT, false);
                mPresenter.changeBottomImg(MESSAGE_PAGE_FRAGMENT);
                break;
            case R.id.btn_mine:
                viewpager.setCurrentItem(MINE_PAGE_FRAGMENT, false);
                mPresenter.changeBottomImg(MINE_PAGE_FRAGMENT);
                break;
        }
    }

    @Override
    public void changeBottomImg(int... res) {
        btnMainPage.setImageBitmap(BitmapFactory.decodeResource(getResources(), res[0]));
        btnFind.setImageBitmap(BitmapFactory.decodeResource(getResources(), res[1]));
        btnMsg.setImageBitmap(BitmapFactory.decodeResource(getResources(), res[2]));
        btnMine.setImageBitmap(BitmapFactory.decodeResource(getResources(), res[3]));
    }

    @Override
    public void onBackPressed() {
        //将当前Activity加入到栈中，而不是退出
        moveTaskToBack(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
