package com.sunny.youyun.fragment.main.mine_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.fragment.MVPBaseFragment;
import com.sunny.youyun.model.User;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.utils.GlideUtils;
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
    @BindView(R.id.li_user_name)
    LineMenuItem liUserName;
    @BindView(R.id.li_bind_phone)
    LineMenuItem liBindPhone;
    @BindView(R.id.li_my_file)
    LineMenuItem liMyFile;
    @BindView(R.id.li_password_change)
    LineMenuItem liPasswordChange;
    @BindView(R.id.li_callback)
    LineMenuItem liCallback;
    @BindView(R.id.li_exit)
    TextView liExit;
    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

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

        User user = UserInfoManager.getInstance().getUserInfo();
        liUserName.setValue(user.getUsername());
        GlideUtils.loadUrl(activity, imgAvatar, user.getAvatar());
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

    @OnClick({R.id.cl_avatar, R.id.li_user_name, R.id.li_bind_phone, R.id.li_my_file, R.id.li_password_change, R.id.li_callback, R.id.li_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_avatar:
                RouterUtils.open(activity, IndexRouter.DcimActivity);
                break;
            case R.id.li_user_name:
                break;
            case R.id.li_bind_phone:
                break;
            case R.id.li_my_file:
                break;
            case R.id.li_password_change:
                break;
            case R.id.li_callback:
                break;
            case R.id.li_exit:
                RouterUtils.open(activity, IndexRouter.LoginActivity);
                break;
        }
    }
}
