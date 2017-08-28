package com.sunny.youyun.activity.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mzule.activityrouter.annotation.Router;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.BaseUiListener;
import com.sunny.youyun.base.MVPBaseActivity;
import com.sunny.youyun.model.QQLoginResult;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.utils.AccountValidatorUtil;
import com.sunny.youyun.utils.GsonUtil;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.share.TencentUtil;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.RichEditText;
import com.tencent.tauth.Tencent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IndexRouter.LoginActivity)
public class LoginActivity extends MVPBaseActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.et_username)
    RichEditText etUsername;
    @BindView(R.id.et_password)
    RichEditText etPassword;
    @BindView(R.id.tv_forget_pass)
    TextView tvForgetPass;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.login));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });

    }

    @Override
    protected LoginPresenter onCreatePresenter() {
        return new LoginPresenter(this);
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
    public void loginSuccess() {
        dismissDialog();
        onBackPressed();
    }

    @OnClick({R.id.tv_forget_pass, R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_pass:
                RouterUtils.open(this, IndexRouter.ForgetPassActivity);
                break;
            case R.id.btn_login:
                String phone = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (phone.equals("") || password.equals("")) {
                    Toast.makeText(this, getString(R.string.please_complete_info), Toast.LENGTH_SHORT).show();
                } else if (AccountValidatorUtil.isMobile(phone)) {
                    showLoading();
                    mPresenter.login(phone, password);
                } else {
                    Toast.makeText(this, getString(R.string.please_input_correct_phone_number), Toast.LENGTH_SHORT).show();
                }
//                TencentUtil.getInstance(this)
//                        .loginOut();
                break;
            case R.id.btn_register:
//                RouterUtils.open(this, IndexRouter.RegisterActivity);
                TencentUtil.getInstance(this)
                        .login(new BaseUiListener(){
                            @Override
                            public void onComplete(Object o) {
                                showLoading();
                                QQLoginResult result = GsonUtil.getInstance()
                                        .fromJson(GsonUtil.getInstance().toJson(o), QQLoginResult.class);
                                Logger.i("result: " + result);
                                YouyunAPI.updateQQLoginResult(result);
                                mPresenter.qqLogin(result);
                            }
                        });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data,
                new BaseUiListener(){
                    @Override
                    public void onComplete(Object o) {
                        QQLoginResult result = GsonUtil.getInstance()
                                .fromJson(GsonUtil.getInstance().toJson(o), QQLoginResult.class);
                        Logger.i("result2: " + result);

                    }
                });
    }
}
