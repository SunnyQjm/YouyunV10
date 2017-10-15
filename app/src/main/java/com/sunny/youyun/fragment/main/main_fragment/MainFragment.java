package com.sunny.youyun.fragment.main.main_fragment;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.download.DownloadActivity;
import com.sunny.youyun.activity.file_manager.FileManagerActivity;
import com.sunny.youyun.activity.file_manager.config.FileManagerRequest;
import com.sunny.youyun.activity.upload_setting.UploadSettingActivity;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.fragment.main.main_fragment.adapter.RecordTabsAdapter;
import com.sunny.youyun.fragment.main.main_fragment.download_record_fragment.DownloadRecordFragment;
import com.sunny.youyun.fragment.main.main_fragment.upload_record_fragment.UploadRecordFragment;
import com.sunny.youyun.internet.upload.FileUploadFileParam;
import com.sunny.youyun.internet.upload.config.UploadConfig;
import com.sunny.youyun.model.event.MultiSelectEvent;
import com.sunny.youyun.utils.DialogUtils;
import com.sunny.youyun.utils.RecyclerViewUtils;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Sunny on 2017/6/24 0024.
 */

public class MainFragment extends MVPBaseFragment<MainFragmentPresenter> implements MainFragmentContract.View {

    Unbinder unbinder;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;
    private RecordTabsAdapter adapter;

    private DownloadRecordFragment downloadRecordFragment;
    private UploadRecordFragment uploadRecordFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private Dialog dialog = null;
    private View.OnClickListener listener;
    private static final int TAB_MARGIN_LEFT = 40;

    private static final int TAB_MARGIN_RIGHT = 40;
    private static final int DURATION = 300;
    public static final int DOWNLOAD_CODE = 233;
    public static final int PATH_S = 234;
    private static final int UPLOAD_SETTING = 235;

    private View view = null;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    private void initView() {
        downloadRecordFragment = DownloadRecordFragment.newInstance();
        uploadRecordFragment = UploadRecordFragment.newInstance();
        fragmentList.add(downloadRecordFragment);
        fragmentList.add(uploadRecordFragment);
        adapter = new RecordTabsAdapter(getChildFragmentManager(), fragmentList);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        viewpager.setScroll(true);
        tabLayout.setupWithViewPager(viewpager);
        //使标题居中且宽度充满全屏
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        RecyclerViewUtils.setIndicator(activity, tabLayout, TAB_MARGIN_LEFT, TAB_MARGIN_RIGHT);

        dialog = new Dialog(activity, R.style.dialog);
        dialog.setOnCancelListener(dialog1 -> closeAdd());
        listener = v -> {
            if (dialog != null)
                dialog.dismiss();
            closeAdd();
            switch (v.getId()) {
                case R.id.et_trans:
                    RouterUtils.open(activity, IntentRouter.SingleTransMainActivity);
                    break;
                case R.id.et_download:
                    startActivityForResult(new Intent(activity, DownloadActivity.class), DOWNLOAD_CODE);
                    break;
                case R.id.et_upload:
                    startActivityForResult(new Intent(activity, FileManagerActivity.class), PATH_S);
                    break;
            }
        };
    }

    private void closeAdd() {
        ObjectAnimator.ofFloat(btnAdd, "rotation", 135, 0)
                .setDuration(DURATION)
                .start();
    }

    @Override
    public void showSuccess(String info) {
        super.showSuccess(info);
    }

    @Override
    public void showError(String info) {
        super.showError(info);
    }

    @Override
    public void showTip(String info) {
        super.showTip(info);
    }

    @Override
    protected MainFragmentPresenter onCreatePresenter() {
        return new MainFragmentPresenter(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PATH_S:        //选择文件的结果
                if (data == null) {
                    Logger.i("PATH_S: data is null");
                    return;
                }
                String[] paths = data.getStringArrayExtra(FileManagerRequest.KEY_PATH);
                Intent intent = new Intent(activity, UploadSettingActivity.class);
                intent.putExtra("paths", paths);
                startActivityForResult(intent, UPLOAD_SETTING);
                break;
            case UPLOAD_SETTING:
                if (data == null) {
                    Logger.i("UPLOAD_SETTING: data is null");
                    return;
                }
                paths = data.getStringArrayExtra(UploadConfig.PATH);
                int allowDownloadCount = data.getIntExtra(UploadConfig.ALLOW_DOWNLOAD_COUNT, -1);
                long expireTime = data.getLongExtra(UploadConfig.EFFECT_DATE, -1);
                boolean isPublic = data.getBooleanExtra(UploadConfig.IS_PUBLIC, true);
                int score = data.getIntExtra(UploadConfig.DOWNLOAD_SCORE, 0);
                String parentId = data.getStringExtra(UploadConfig.PARENT_ID);
                String description = data.getStringExtra(UploadConfig.DESCRIPTION);
                for (String path : paths) {
                    mPresenter.uploadFile(new FileUploadFileParam
                            .Builder()
                            .filePath(path)
                            .allowDownCount(allowDownloadCount)
                            .expireTime(expireTime)
                            .isPrivate(!isPublic)
                            .isShare(isPublic)
                            .parentId(parentId)
                            .description(description)
                            .score(score)
                            .build());
                }
                break;
        }
    }

    @Override
    public void uploadSuccess(String info) {
        dismissDialog();
        Toast.makeText(activity, info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        DialogUtils.showSelectDialog(activity, dialog, listener);
        ObjectAnimator.ofFloat(btnAdd, "rotation", 0, 135)
                .setDuration(DURATION)
                .start();
    }


    /**
     * 显示多选器
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MultiSelect(MultiSelectEvent event) {
        switch (event.operator) {
            //显示的时候不允许滚动
            case SHOW:
                viewpager.setScroll(false);
                break;
            //隐藏的时候允许滚动
            case HIDE:
                viewpager.setScroll(true);
                break;
        }
    }

}
