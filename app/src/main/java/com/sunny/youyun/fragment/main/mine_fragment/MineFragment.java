package com.sunny.youyun.fragment.main.mine_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.model.User;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.LineMenuItem;
import com.sunny.youyun.views.YouyunEditDialog;

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
    @BindView(R.id.img_avatar)
    ImageView imgAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.img_qr_code)
    ImageView imgQrCode;
    @BindView(R.id.li_my_share)
    LineMenuItem liMyShare;
    @BindView(R.id.li_file_manager)
    LineMenuItem liAboutYouyun;
    @BindView(R.id.img_edit)
    ImageView imgEdit;


    private YouyunEditDialog editDialog = null;
    private User user;

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

        user = UserInfoManager.getInstance().getUserInfo();
        if (YouyunAPI.isIsLogin()){
            tvNickname.setText(user.getUsername());
            GlideUtils.load(activity, imgAvatar, user.getAvatar());
        }
        else {
            tvNickname.setText(getString(R.string.click_here_to_login));
            GlideUtils.load(activity, imgAvatar, R.drawable.icon_logo_round);
        }
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
            R.id.img_avatar})
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
                RouterUtils.open(activity, IntentRouter.MyCollectionActivity);
                break;
            case R.id.li_my_concern:
                RouterUtils.open(activity, IntentRouter.ConcernActivity);
                break;
            case R.id.li_callback:
                break;
            case R.id.li_setting:
                RouterUtils.open(activity, IntentRouter.SettingActivity);
                break;
            case R.id.li_my_share:
                break;
            case R.id.li_file_manager:
                RouterUtils.open(activity, IntentRouter.PersonFileManagerActivity);
                break;
            case R.id.img_edit:
                if (YouyunAPI.isIsLogin()) {
                    showEditDialog(user.getUsername());
                }
                break;
            case R.id.img_avatar:
                RouterUtils.open(activity, IntentRouter.DcimActivity);
                break;
        }
    }

    private void showEditDialog(String nickname) {
        if (editDialog == null)
            editDialog = YouyunEditDialog.newInstance(getString(R.string.edit_nickname),
                    nickname, System.out::println);
        else {
            editDialog.setHint(nickname);
        }
        editDialog.show(getFragmentManager(), String.valueOf(this.getClass()));
    }


}
