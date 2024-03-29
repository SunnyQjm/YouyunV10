package com.sunny.youyun.activity.person_setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.model.User;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.idling.EspressoIdlingResource;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.ExpandableLineMenuItem;
import com.sunny.youyun.views.LineMenuItem;
import com.sunny.youyun.views.youyun_dialog.edit.YouyunEditDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IntentRouter.PersonSettingActivity)
public class PersonSettingActivity extends MVPBaseActivity<PersonSettingPresenter>
        implements PersonSettingContract.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.li_change_avatar)
    LineMenuItem liChangeAvatar;
    @BindView(R.id.li_change_nickname)
    LineMenuItem liChangeNickname;
    @BindView(R.id.person_setting_signature_et)
    EditText etSignature;
    @BindView(R.id.person_setting_signature_img_sure)
    ImageView personSettingSignatureImgSure;
    @BindView(R.id.person_setting_signature)
    ExpandableLineMenuItem personSettingSignature;
    @BindView(R.id.li_change_qq)
    LineMenuItem liChangeQq;
    @BindView(R.id.li_phone)
    LineMenuItem liPhone;
    @BindView(R.id.li_change_password)
    LineMenuItem liChangePassword;
    @BindView(R.id.li_email)
    LineMenuItem liEmail;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.person_info_edit));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {
            }
        });
        fillData();
        etSignature.setSelection(etSignature.getText().toString().length());
    }

    @Override
    protected PersonSettingPresenter onCreatePresenter() {
        return new PersonSettingPresenter(this);
    }

    @OnClick({R.id.li_change_avatar, R.id.li_change_nickname})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.li_change_avatar:
                RouterUtils.openForResult(this, IntentRouter.DcimActivity, 0);
                break;
            case R.id.li_change_nickname:
                YouyunEditDialog.newInstance(getString(R.string.edit_nickname),
                        user.getUsername(), result -> {
                            if (result == null || result.equals("")) {
                                showError("不能为空");
                                return;
                            }
                            getMPresenter().modifyUserName(result);
                        })
                        .show(getSupportFragmentManager(), this.getClass().toString());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果是更新头像的请求，则修改成功后填充一下数据
        if (requestCode == 0)
            fillData();
    }

    @Override
    public void modifyUserInfoSuccess() {
        showSuccess(getString(R.string.modify_success));
        fillData();
        EspressoIdlingResource.getInstance()
                .decrement();
    }

    private void fillData() {
        user = UserInfoManager.getInstance()
                .getUserInfo();
        GlideUtils.load(this, liChangeAvatar.getRight_icon(), user.getAvatar());
        liChangeNickname.setValue(user.getUsername());
        etSignature.setText(user.getSignature());
        if (user.getPhone() == null || user.getPhone().equals(""))
            liPhone.setValue(getString(R.string.not_bind));
        else
            liPhone.setValue(user.getPhone());
        if (user.getEmail() == null || user.getEmail().equals(""))
            liEmail.setValue(getString(R.string.not_bind));
        else
            liEmail.setValue(user.getEmail());
        if (user.getQqNumber() == null || user.getQqNumber().equals(""))
            liChangeQq.setValue(getString(R.string.not_bind));
        else
            liChangeQq.setValue(user.getQqNumber());
    }

    /**
     * 修改个签
     */
    @OnClick(R.id.person_setting_signature_img_sure)
    public void onPersonSettingSignatureImgSureClicked() {
        getMPresenter().modifySignature(etSignature.getText().toString());
    }

    /**
     * 改变qq
     */
    @OnClick(R.id.li_change_qq)
    public void onLiChangeQqClicked() {
    }

    /**
     * 改变手机号
     */
    @OnClick(R.id.li_phone)
    public void onLiPhoneClicked() {
    }

    /**
     * 更改密码
     */
    @OnClick(R.id.li_change_password)
    public void onLiChangePasswordClicked() {
    }

    /**
     * 更改邮箱
     */
    @OnClick(R.id.li_email)
    public void onEmailClicked() {
    }
}
