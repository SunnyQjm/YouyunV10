package com.sunny.youyun.fragment.main.mine_fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.scan.ScanActivity;
import com.sunny.youyun.activity.scan.config.ScanConfig;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.model.User;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.model.result.ScanResult;
import com.sunny.youyun.utils.EasyPermission;
import com.sunny.youyun.utils.GlideOptions;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.GsonUtil;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.LineMenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Sunny on 2017/6/25 0025.
 */

public class MineFragment extends MVPBaseFragment<MinePresenter> implements MineContract.View {

    Unbinder unbinder;
    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.cl_avatar)
    ConstraintLayout clAvatar;
    @BindView(R.id.li_my_collect)
    LineMenuItem liMyCollect;
    @BindView(R.id.li_my_concern)
    LineMenuItem liMyConcern;
    @BindView(R.id.li_callback)
    LineMenuItem liCallback;
    @BindView(R.id.li_setting)
    LineMenuItem liSetting;
    @BindView(R.id.img_icon)
    ImageView imgAvatar;
    @BindView(R.id.tv_name)
    TextView tvNickname;
    @BindView(R.id.img_qr_code)
    ImageView imgQrCode;
    @BindView(R.id.li_my_share)
    LineMenuItem liMyShare;
    @BindView(R.id.li_file_manager)
    LineMenuItem liAboutYouyun;
    @BindView(R.id.img_edit)
    ImageView imgEdit;


    private User user;
    private static final int REQUEST_CODE_SCAN = 0;

    public static MineFragment newInstance() {

        Bundle args = new Bundle();

        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            onResume();
    }

    private View view = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_mine, container, false);
            unbinder = ButterKnife.bind(this, view);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }

        return view;
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.person_center));
        easyBar.setLeftIconInVisible();
        easyBar.setRightIconVisible();
        easyBar.setRightIcon(R.drawable.icon_mine_scan);
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {

            }

            @Override
            public void onRightIconClick(View view) {
                //TODO open scan
                MineFragment.this.startActivityForResult(new Intent(activity, ScanActivity.class),
                        REQUEST_CODE_SCAN);
            }
        });

        user = UserInfoManager.getInstance().getUserInfo();
        if (YouyunAPI.isIsLogin()) {
            tvNickname.setText(user.getUsername());
            GlideUtils.load(activity, imgAvatar, user.getAvatar());
        } else {
            tvNickname.setText(getString(R.string.click_here_to_login));
            GlideUtils.load(activity, imgAvatar, R.drawable.icon_logo_round);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (requestCode == REQUEST_CODE_SCAN) {
            String result = data.getStringExtra(ScanConfig.SCAN_RESULT);
            ScanResult scanResult = GsonUtil.json2Bean(result, ScanResult.class);
            if(scanResult == null)
                return;
            switch (scanResult.getType()) {
                case ScanResult.TYPE_FILE:
                    RouterUtils.openToFileDetailOnline(activity, Integer.valueOf(scanResult.getData()),
                            scanResult.getData2());
                    break;
                case ScanResult.TYPE_USER:
                    RouterUtils.openToUser(activity, Integer.parseInt(scanResult.getData()));
                    break;
                case ScanResult.TYPE_URL:
                    break;
            }

        }
    }

    @Override
    protected MinePresenter onCreatePresenter() {
        return new MinePresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.cl_avatar, R.id.li_my_collect, R.id.li_my_concern, R.id.li_callback,
            R.id.li_setting, R.id.li_my_share, R.id.li_file_manager, R.id.img_edit,
            R.id.img_icon, R.id.img_qr_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_avatar:
                if (YouyunAPI.isIsLogin()) {
                    RouterUtils.open(activity, IntentRouter.PersonInfoActivity);
                } else {
                    RouterUtils.open(activity, IntentRouter.LoginActivity);
                }
                break;
            case R.id.li_my_collect:
                RouterUtils.openAfterLogin(activity, IntentRouter.MyCollectionActivity);
                break;
            case R.id.li_my_concern:
                RouterUtils.openAfterLogin(activity, IntentRouter.ConcernActivity);
                break;
            case R.id.li_callback:
                break;
            case R.id.li_setting:
                RouterUtils.open(activity, IntentRouter.SettingActivity);
                break;
            case R.id.li_my_share:
                break;
            case R.id.li_file_manager:
                RouterUtils.openAfterLogin(activity, IntentRouter.PersonFileManagerActivity);
                break;
            case R.id.img_edit:
                RouterUtils.openAfterLogin(activity, IntentRouter.PersonSettingActivity);
                break;
            case R.id.img_icon:
                RouterUtils.openAfterLogin(activity, IntentRouter.DcimActivity);
                break;
            case R.id.img_qr_code:  //显示二维码
                if (!YouyunAPI.isIsLogin())
                    return;
                EasyPermission.checkAndRequestREAD_WRITE_EXTENAL(activity, new EasyPermission.OnPermissionRequestListener() {
                    @Override
                    public void success() {
                        displayQRCode();
                    }

                    @Override
                    public void fail() {

                    }
                });
                break;
        }
    }

    private void displayQRCode() {
        //将圆形头像放在二维码中间
        Glide.with(activity)
                .asBitmap()
                .apply(GlideOptions.getInstance()
                        .getAvatarOptions())
                .load(user.getAvatar())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        ScanResult scanResult = new ScanResult.Builder()
                                .data(String.valueOf(UserInfoManager.getInstance().getUserId()))
                                .type(ScanResult.TYPE_USER)
                                .build();
                        showQrDialog(GsonUtil.bean2Json(scanResult))
                                .setCenterIcon(resource);
                    }
                });
    }
}
