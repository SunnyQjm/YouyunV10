package com.sunny.youyun.activity.person_info;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.person_info.adapter.RecordTabsAdapter;
import com.sunny.youyun.activity.person_info.concern_fragment.ConcernFragment;
import com.sunny.youyun.activity.person_info.dynamic_fragment.DynamicFragment;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.model.User;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.model.result.GetUserInfoResult;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.RecyclerViewUtils;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.EasyBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(value = {IntentRouter.PersonInfoActivity, IntentRouter.PersonInfoActivity + "/:otherId"},
        intParams = "otherId")
public class PersonInfoActivity extends MVPBaseActivity<PersonInfoPresenter> implements PersonInfoContract.View,
        MVPBaseFragment.OnFragmentInteractionListener {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.img_icon)
    ImageView imgAvatar;
    @BindView(R.id.tv_name)
    TextView tvNickname;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.following_num)
    TextView followingNum;
    @BindView(R.id.fans_num)
    TextView fansNum;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.person_info_concern_icon)
    ImageView personInfoConcernIcon;
    @BindView(R.id.person_info_concern_text)
    TextView personInfoConcernText;
    @BindView(R.id.person_info_concern)
    LinearLayout personInfoConcern;
    @BindView(R.id.person_info_private_letter_icon)
    ImageView personInfoPrivateLetterIcon;
    @BindView(R.id.person_info_private_letter_text)
    TextView personInfoPrivateLetterText;
    @BindView(R.id.person_info_private_letter)
    LinearLayout personInfoPrivateLetter;
    @BindView(R.id.person_info_bottom_bar)
    LinearLayout personInfoBottomBar;


    private List<Fragment> fragmentList = new ArrayList<>();
    private ConcernFragment concernFragment;
    private DynamicFragment dynamicFragment;
    private RecordTabsAdapter adapter;
    //用来标识是否访问的是别人的主页
    private int otherId = -1;

    private static final int TAB_MARGIN_LEFT = 40;
    private static final int TAB_MARGIN_RIGHT = 40;
    private User user;

    private static final int REQUEST_EDIT_INFO = 100;

    @Override
    protected void onStart() {
        super.onStart();
        if (otherId > 0) { //查看别人的信息
            mPresenter.getOtherUserInfoOnline(otherId);
        } else {  //查看自己的信息
            mPresenter.getUserInfoOnline();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });

        otherId = getIntent().getIntExtra("otherId", -1);
        if (otherId > 0 && otherId != UserInfoManager.getInstance()
                .getUserInfo().getId()) {  //如果查看的是别人的信息
            imgEdit.setVisibility(View.INVISIBLE);
            personInfoBottomBar.setVisibility(View.VISIBLE);
        } else {
            personInfoBottomBar.setVisibility(View.INVISIBLE);
        }

        concernFragment = ConcernFragment.newInstance();
        dynamicFragment = DynamicFragment.newInstance();
        fragmentList.add(dynamicFragment);
        fragmentList.add(concernFragment);
        adapter = new RecordTabsAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        tabLayout.setupWithViewPager(viewpager);
        //使标题居中且宽度充满全屏
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        RecyclerViewUtils.setIndicator(this, tabLayout, TAB_MARGIN_LEFT, TAB_MARGIN_RIGHT);

        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.bar_title_style_collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.bar_title_style_expand);

    }

    @Override
    protected PersonInfoPresenter onCreatePresenter() {
        return new PersonInfoPresenter(this);
    }

    @OnClick({R.id.img_icon, R.id.tv_name, R.id.tv_signature, R.id.img_edit,
            R.id.person_info_concern, R.id.person_info_private_letter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_icon:
                break;
            case R.id.tv_name:
                break;
            case R.id.tv_signature:
                break;
            case R.id.img_edit:
                if (YouyunAPI.isIsLogin()) {
                    RouterUtils.openForResult(this, IntentRouter.PersonSettingActivity,
                            REQUEST_EDIT_INFO);
                }
                break;
            case R.id.person_info_concern:
                //TODO 关注
                if (otherId < 0)
                    return;
                mPresenter.concern(otherId);
                break;
            case R.id.person_info_private_letter:
                RouterUtils.open(this, IntentRouter.ChatActivity,
                        String.valueOf(user.getId()), user.getUsername());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_INFO) {
            fillData(UserInfoManager.getInstance()
                    .getUserInfo());
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void getUserInfoSuccess(GetUserInfoResult result) {
        User user = result.getUser();
        getOtherUserInfoSuccess(user);
    }

    @Override
    public void getOtherUserInfoSuccess(User user) {
        if (otherId < 0) { //如果获取的是自己的信息则保存到本地
            //更新本地的用户信息
            UserInfoManager.getInstance()
                    .setUserInfo(user);
        }
        fillData(user);
    }

    @Override
    public void concernSuccess() {
        if (user == null)
            return;
        if (user.isFollow()) {
            user.setFollow(false);
        } else {
            user.setFollow(true);
        }
        fillData(user);
    }

    private void fillData(User user) {
        this.user = user;
        GlideUtils.load(this, imgAvatar, user.getAvatar());
        tvNickname.setText(user.getUsername());
        tvSignature.setText(user.getSignature());
        fansNum.setText(String.format(getString(R.string.fans_num), " ", user.getFans()));
        followingNum.setText(String.format(getString(R.string.concern_num), " ", user.getFollowers()));
        collapsingToolbarLayout.setTitle(user.getUsername());
        if (otherId > 0) {
            if (user.isFollow()) {
                personInfoConcernText.setText(getString(R.string.cancel_concern));
                personInfoConcernIcon.setImageResource(R.drawable.icon_user_follow_selected);
            } else {
                personInfoConcernIcon.setImageResource(R.drawable.icon_user_follow);
                personInfoConcernText.setText(getString(R.string.concern));
            }
        }
    }
}
