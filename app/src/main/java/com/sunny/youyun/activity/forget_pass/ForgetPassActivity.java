package com.sunny.youyun.activity.forget_pass;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.MVPBaseActivity;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.RichEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IndexRouter.ForgetPassActivity)
public class ForgetPassActivity extends MVPBaseActivity<ForgetPassPresenter> implements ForgetPassContract.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.et_username)
    RichEditText etUsername;
    @BindView(R.id.et_password)
    RichEditText etPassword;
    @BindView(R.id.et_confirm_code)
    RichEditText etConfirmCode;
    @BindView(R.id.et_confirm_pass)
    RichEditText etConfirmPass;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.forget_pass));
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
    protected ForgetPassPresenter onCreatePresenter() {
        return new ForgetPassPresenter(this);
    }

    @OnClick({R.id.btn_login, R.id.tv_send_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                break;
            case R.id.tv_send_code:
                break;
        }
    }
}
