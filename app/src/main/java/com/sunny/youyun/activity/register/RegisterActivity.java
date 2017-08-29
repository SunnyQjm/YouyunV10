package com.sunny.youyun.activity.register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.utils.AccountValidatorUtil;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.RichEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IndexRouter.RegisterActivity)
public class RegisterActivity extends MVPBaseActivity<RegisterPresenter> implements RegisterContract.View {
    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.et_username)
    RichEditText etUsername;
    @BindView(R.id.et_password)
    RichEditText etPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.et_confirm_code)
    RichEditText etConfirmCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.register));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });

        etUsername.requestFocus();
    }

    @Override
    protected RegisterPresenter onCreatePresenter() {
        return new RegisterPresenter(this);
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
    public void registerSuccess(String info) {
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btn_register, R.id.tv_send_code})
    public void onViewClicked(View view) {
        String phone = etUsername.getText().toString();
        switch (view.getId()) {
            case R.id.btn_register:
                String password = etPassword.getText().toString();
                String code = etConfirmCode.getText().toString();
                if (phone.equals("") || password.equals("") || code.equals("")) {
                    Toast.makeText(this, getString(R.string.please_complete_info), Toast.LENGTH_SHORT).show();
                } else if (AccountValidatorUtil.isMobile(phone)) {
                    mPresenter.register(phone, phone, password, code);
                } else {
                    Toast.makeText(this, getString(R.string.please_input_correct_phone_number), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_send_code:
                if (phone.equals("")){
                    Toast.makeText(this, getString(R.string.phone_number_not_allow_empty), Toast.LENGTH_SHORT).show();
                } else if (AccountValidatorUtil.isMobile(phone)) {
                    mPresenter.sendCode(phone);
                } else {
                    Toast.makeText(this, getString(R.string.please_input_correct_phone_number), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
