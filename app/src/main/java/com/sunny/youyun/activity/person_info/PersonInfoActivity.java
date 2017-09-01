package com.sunny.youyun.activity.person_info;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.fragment.main.main_fragment.Adapter.RecordTabsAdapter;
import com.sunny.youyun.fragment.main.main_fragment.DownloadReccordFragment.DownloadRecordFragment;
import com.sunny.youyun.fragment.main.main_fragment.UploadRecordFragment.UploadRecordFragment;
import com.sunny.youyun.model.User;
import com.sunny.youyun.model.result.GetUserInfoResult;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.RecyclerViewUtils;
import com.sunny.youyun.views.EasyBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IntentRouter.PersonInfoActivity)
public class PersonInfoActivity extends MVPBaseActivity<PersonInfoPresenter> implements PersonInfoContract.View,
        MVPBaseFragment.OnFragmentInteractionListener {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.img_avatar)
    ImageView imgAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    private List<Fragment> fragmentList = new ArrayList<>();
    private DownloadRecordFragment downloadRecordFragment;
    private UploadRecordFragment uploadRecordFragment;
    private RecordTabsAdapter adapter;

    private static final int TAB_MARGIN_LEFT = 40;
    private static final int TAB_MARGIN_RIGHT = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle("我的");
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });

        downloadRecordFragment = DownloadRecordFragment.newInstance();
        uploadRecordFragment = UploadRecordFragment.newInstance();
        fragmentList.add(downloadRecordFragment);
        fragmentList.add(uploadRecordFragment);
        adapter = new RecordTabsAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        tabLayout.setupWithViewPager(viewpager);
        //使标题居中且宽度充满全屏
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        RecyclerViewUtils.setIndicator(this, tabLayout, TAB_MARGIN_LEFT, TAB_MARGIN_RIGHT);

        mPresenter.getUserInfoOnline();
    }

    @Override
    protected PersonInfoPresenter onCreatePresenter() {
        return new PersonInfoPresenter(this);
    }

    @OnClick({R.id.img_avatar, R.id.tv_nickname, R.id.tv_signature})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_avatar:
                break;
            case R.id.tv_nickname:
                break;
            case R.id.tv_signature:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void getUserInfoSuccess(GetUserInfoResult result) {
        User user = result.getUser();
        easyBar.setTitle(user.getUsername());
        tvNickname.setText(user.getUsername());
        GlideUtils.loadUrl(this, imgAvatar, user.getAvatar());
    }
}
