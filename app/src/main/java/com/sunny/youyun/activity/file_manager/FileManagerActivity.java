package com.sunny.youyun.activity.file_manager;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.adpater.RecordTabsAdapter;
import com.sunny.youyun.activity.file_manager.config.FileManagerRequest;
import com.sunny.youyun.activity.file_manager.fragment.application.ApplicationFragment;
import com.sunny.youyun.activity.file_manager.fragment.document.DocumentFragment;
import com.sunny.youyun.activity.file_manager.fragment.other.OtherFragment;
import com.sunny.youyun.activity.file_manager.fragment.picture.PictureFragment;
import com.sunny.youyun.activity.file_manager.fragment.video.VideoFragment;
import com.sunny.youyun.activity.file_manager.manager.CheckStateManager;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.utils.DensityUtil;
import com.sunny.youyun.utils.EasyPermission;
import com.sunny.youyun.utils.RecyclerViewUtils;
import com.sunny.youyun.views.EasyBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IntentRouter.FileManagerActivity)
public class FileManagerActivity extends MVPBaseActivity<FileManagerPresenter> implements FileManagerContract.View, MVPBaseFragment.OnFragmentInteractionListener {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.easyBar)
    EasyBar easyBar;
    private RecordTabsAdapter adapter;
    private ApplicationFragment applicationFragment;
    private DocumentFragment documentFragment;
    private OtherFragment otherFragment;
    private PictureFragment pictureFragment;
    private VideoFragment videoFragment;
    private List<Fragment> fragmentList = new ArrayList<>();

    private static final int TAB_MARGIN_LEFT = 5;
    private static final int TAB_MARGIN_RIGHT = 5;

    @Override
    @RequiresPermission(anyOf = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.blue, null));
        } else {
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.blue));
        }
        ButterKnife.bind(this);
        //init check manager
        CheckStateManager.init();
        initView();
    }

    private MVPBaseFragment getFragment(int position) {
        switch (position) {
            case 0:
                return videoFragment;
            case 1:
                return pictureFragment;
            case 2:
                return documentFragment;
            case 3:
                return applicationFragment;
            case 4:
                return otherFragment;
            default:
                return null;
        }
    }

    private void initView() {
        EasyPermission.checkAndRequestREAD_WRITE_EXTENAL(this);
        easyBar.setTitle(getString(R.string.device_file));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });
        videoFragment = VideoFragment.newInstance();
        fragmentList.add(videoFragment);
        pictureFragment = PictureFragment.newInstance();
        fragmentList.add(pictureFragment);
        documentFragment = DocumentFragment.newInstance();
        fragmentList.add(documentFragment);
        applicationFragment = ApplicationFragment.newInstance();
        fragmentList.add(applicationFragment);
        otherFragment = OtherFragment.newInstance();
        fragmentList.add(otherFragment);

        String[] titles = {"影音", "图片", "文档", "应用", "其它"};
        adapter = new RecordTabsAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        tabLayout.setupWithViewPager(viewpager);
        //使标题居中且宽度充满全屏
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        RecyclerViewUtils.setIndicator(this, tabLayout,
                DensityUtil.dip2px(this, TAB_MARGIN_LEFT), DensityUtil.dip2px(this, TAB_MARGIN_RIGHT));

    }

    @Override
    public void onBackPressed() {
        MVPBaseFragment baseFragment = getFragment(viewpager.getCurrentItem());
        if (baseFragment == null || !baseFragment.onBackPressed())
            super.onBackPressed();
    }

    @Override
    protected FileManagerPresenter onCreatePresenter() {
        return new FileManagerPresenter(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        int num = CheckStateManager.getInstance().getResultNum();
        if (num == 0) {     //select none
            tvSure.setText(getString(R.string.sure));
        } else {            //select same file
            String text = getString(R.string.sure) + "(已选" + num + "个）";
            Spannable spannable = new SpannableString(text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_gray, null)),
                        getString(R.string.sure).length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_gray)),
                        getString(R.string.sure).length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tvSure.setText(spannable);
        }
    }


    @OnClick({R.id.tv_cancel, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_sure:
                Intent intent = new Intent();
                intent.putExtra(FileManagerRequest.KEY_PATH, CheckStateManager.getInstance().result());
                setResult(0, intent);
                finish();
                break;
        }
    }

    @Override
    public void showSuccess(String info) {

    }

    @Override
    public void showError(String info) {

    }

    @Override
    public void showTip(String info) {

    }
}
