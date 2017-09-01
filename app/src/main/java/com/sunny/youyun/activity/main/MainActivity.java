package com.sunny.youyun.activity.main;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.githang.statusbar.StatusBarCompat;
import com.github.mzule.activityrouter.annotation.Router;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.main.config.MainActivityConfig;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.fragment.main.finding_fragment.FindingFragment;
import com.sunny.youyun.fragment.main.main_fragment.MainFragment;
import com.sunny.youyun.fragment.main.message_fragment.MessageFragment;
import com.sunny.youyun.fragment.main.mine_fragment.MineFragment;
import com.sunny.youyun.utils.MyNotifyUtil;
import com.sunny.youyun.utils.TimePickerUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IntentRouter.MainActivity)
public class MainActivity extends MVPBaseActivity<MainPresenter> implements MainContract.View, MVPBaseFragment.OnFragmentInteractionListener {


    private static final int MAIN_PAGE_FRAGMENT = 0;
    private static final int FINDING_PAGE_FRAGMENT = 1;
    private static final int MESSAGE_PAGE_FRAGMENT = 2;
    private static final int MINE_PAGE_FRAGMENT = 3;

    @BindView(R.id.fragmentContainer)
    FrameLayout fragmentContainer;
    @BindView(R.id.btn_main_page)
    ImageView btnMainPage;
    @BindView(R.id.btn_find)
    ImageView btnFind;
    @BindView(R.id.btn_msg)
    ImageView btnMsg;
    @BindView(R.id.btn_mine)
    ImageView btnMine;

    private FragmentManager fragmentManager;
    private MainFragment mainFragment;
    private FindingFragment findingFragment;
    private MessageFragment messageFragment;
    private MineFragment mineFragment;

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
        if(tag == null || tag.equals(""))
            return;
        switch (tag) {
            case MainActivityConfig.LUNCH_TAG_UPLOAD_DOWNLOAD:
                selectTab(0);
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
        if (savedInstanceState == null)
            initView();
        else
            reCallInit();
    }

    /**
     * 内存重启时处理
     */
    private void reCallInit() {
        fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        mainFragment = (MainFragment) fragmentManager.getFragment(bundle, MainFragment.class.getName());
        findingFragment = (FindingFragment) fragmentManager.getFragment(bundle, FindingFragment.class.getName());
        messageFragment = (MessageFragment) fragmentManager.getFragment(bundle, MessageFragment.class.getName());
        mineFragment = (MineFragment) fragmentManager.getFragment(bundle, MineFragment.class.getName());
    }

    private void initView() {
        System.out.println("initView");
        fragmentManager = getSupportFragmentManager();
        selectTab(MAIN_PAGE_FRAGMENT);
    }

    private void hideAll(FragmentTransaction ft) {
        if (mainFragment != null) {
            ft.hide(mainFragment);
        }
        if (findingFragment != null) {
            ft.hide(findingFragment);
        }
        if (messageFragment != null) {
            ft.hide(messageFragment);
        }
        if (mineFragment != null) {
            ft.hide(mineFragment);
        }
    }

    private void selectTab(int position) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //先隐藏所有的Fragment
        hideAll(ft);
        switch (position) {
            case MAIN_PAGE_FRAGMENT:
                if (mainFragment == null) {
                    mainFragment = MainFragment.newInstance();
                    ft.add(R.id.fragmentContainer, mainFragment, MainFragment.class.getName());
                    Logger.i("add MAIN");
                } else {
                    ft.show(mainFragment);
                }
                break;
            case FINDING_PAGE_FRAGMENT:
                if (findingFragment == null) {
                    findingFragment = FindingFragment.newInstance();
                    ft.add(R.id.fragmentContainer, findingFragment, FindingFragment.class.getName());
                    Logger.i("add FIND");
                } else {
                    ft.show(findingFragment);
                }
                break;
            case MESSAGE_PAGE_FRAGMENT:
                if (messageFragment == null) {
                    messageFragment = MessageFragment.newInstance();
                    ft.add(R.id.fragmentContainer, messageFragment, MessageFragment.class.getName());
                    Logger.i("add MSG");
                } else {
                    ft.show(messageFragment);
                }
                break;
            case MINE_PAGE_FRAGMENT:
                if (mineFragment == null) {
                    mineFragment = MineFragment.newInstance();
                    ft.add(R.id.fragmentContainer, mineFragment, MineFragment.class.getName());
                    Logger.i("add Mine");
                } else {
                    ft.show(mineFragment);
                }
                break;
        }
        ft.commit();
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
                selectTab(MAIN_PAGE_FRAGMENT);
                mPresenter.changeBottomImg(MAIN_PAGE_FRAGMENT);
                break;
            case R.id.btn_find:
                selectTab(FINDING_PAGE_FRAGMENT);
                mPresenter.changeBottomImg(FINDING_PAGE_FRAGMENT);
                break;
            case R.id.btn_msg:
                selectTab(MESSAGE_PAGE_FRAGMENT);
                mPresenter.changeBottomImg(MESSAGE_PAGE_FRAGMENT);
                break;
            case R.id.btn_mine:
                selectTab(MINE_PAGE_FRAGMENT);
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
