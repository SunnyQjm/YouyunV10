package com.sunny.youyun.activity.main;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.sunny.youyun.model.event.JPushEvent;
import com.sunny.youyun.model.event.MultiSelectEvent;
import com.sunny.youyun.model.manager.MessageManager;
import com.sunny.youyun.utils.DensityUtil;
import com.sunny.youyun.utils.MyNotifyUtil;
import com.sunny.youyun.utils.TimePickerUtils;
import com.sunny.youyun.utils.bus.MessageEventBus;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.NoScrollViewPager;
import com.sunny.youyun.views.drag_view.DraggableFlagView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    //多选器
    private static final long MULTI_SELECT_DURATION = 300;
    private static final int bottom_bar_height = 45;


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
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.l_multi_selector_operator)
    LinearLayout lMultiSelectorOperator;
    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.draggableView)
    DraggableFlagView draggableView;

    private List<Fragment> fragmentList = new ArrayList<>();
    private MainAdapter mainAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        //设置显示标记，如果不在上传下载界面，就在状态栏弹窗提示
        MyNotifyUtil.setShowTag(MyNotifyUtil.SHOW_TAG_MAIN);
        TimePickerUtils.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        MessageEventBus.getInstance()
                .register(this);
        //每次重新可见的时候刷新未读气泡数值
        changeDragCount();
        System.out.println("MainActivity onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        MessageEventBus.getInstance()
                .unregister(this);
        System.out.println("MainActivity onStop");
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
        draggableView.setDraggable(false);
        easyBar.setDisplayMode(EasyBar.Mode.TEXT);
        easyBar.setLeftText("");
        easyBar.setRightText(getString(R.string.select_all));
        easyBar.setTitle("");
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                //TODO cancel
                EventBus.getDefault()
                        .post(new MultiSelectEvent(MultiSelectEvent.Operator.HIDE));
            }

            @Override
            public void onRightIconClick(View view) {
                //TODO select all
                EventBus.getDefault()
                        .post(new MultiSelectEvent(MultiSelectEvent.Operator.SELECT_ALL));
            }
        });
        //设置消失和透明
        easyBar.setVisibility(View.GONE);
        easyBar.setAlpha(0);
        MainFragment mainFragment = MainFragment.newInstance();
        FindingFragment findingFragment = FindingFragment.newInstance();
        MessageFragment messageFragment = MessageFragment.newInstance();
        MineFragment mineFragment = MineFragment.newInstance();
        fragmentList.add(mainFragment);
        fragmentList.add(findingFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(mineFragment);
        mainAdapter = new MainAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(mainAdapter);
        viewpager.setScroll(false);
        viewpager.setCurrentItem(MAIN_PAGE_FRAGMENT, false);

        changeDragCount();
    }

    /**
     * 修改拖拽气泡的数值
     */
    private void changeDragCount() {
        int total = MessageManager.getInstance().getTotalCount();
        if(total > 0){
            draggableView.setVisibility(View.VISIBLE);
            draggableView.setText(String.valueOf(total));
        } else {
            draggableView.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void showTip(String info) {
        super.showTip(info);
    }

    @OnClick({R.id.btn_main_page, R.id.btn_find, R.id.btn_msg, R.id.btn_mine,
            R.id.tv_cancel, R.id.tv_delete})
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
            case R.id.tv_cancel:
                EventBus.getDefault()
                        .post(new MultiSelectEvent(MultiSelectEvent.Operator.HIDE));
                break;
            case R.id.tv_delete:
                //发送删除信号
                EventBus.getDefault()
                        .post(new MultiSelectEvent(MultiSelectEvent.Operator.DELETE));
                //发送隐藏多选器信号
                EventBus.getDefault()
                        .post(new MultiSelectEvent(MultiSelectEvent.Operator.HIDE));
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

    /**
     * 显示多选器
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showMultiSelect(MultiSelectEvent event) {
        if (event.operator != MultiSelectEvent.Operator.SHOW)
            return;
        easyBar.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(easyBar, "alpha", 0f, 1f)
                .setDuration(MULTI_SELECT_DURATION)
                .start();

        lMultiSelectorOperator.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(lMultiSelectorOperator, "translationY",
                DensityUtil.dip2px(this, bottom_bar_height), 0)
                .setDuration(MULTI_SELECT_DURATION)
                .start();
    }

    /**
     * 隐藏多选器
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hideMultiSelect(MultiSelectEvent event) {
        if (event.operator != MultiSelectEvent.Operator.HIDE)
            return;
        ObjectAnimator.ofFloat(easyBar, "alpha", 1f, 0f)
                .setDuration(MULTI_SELECT_DURATION)
                .start();
        easyBar.postDelayed(() -> easyBar.setVisibility(View.GONE), MULTI_SELECT_DURATION);
        ObjectAnimator.ofFloat(lMultiSelectorOperator, "translationY",
                0, DensityUtil.dip2px(this, bottom_bar_height))
                .setDuration(MULTI_SELECT_DURATION)
                .start();
        lMultiSelectorOperator.postDelayed(() -> {
            lMultiSelectorOperator.setVisibility(View.GONE);
        }, MULTI_SELECT_DURATION);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMessage(JPushEvent jPushEvent){
        changeDragCount();
        switch (jPushEvent.getType()){
            case JPushEvent.COMMENT:
                break;
            case JPushEvent.FOLLOW:
                break;
            case JPushEvent.INSTANTCONTACT:
                break;
            case JPushEvent.STAR:
                break;
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
