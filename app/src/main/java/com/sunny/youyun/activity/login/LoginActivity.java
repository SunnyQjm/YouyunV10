package com.sunny.youyun.activity.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mzule.activityrouter.annotation.Router;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.BaseUiListener;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.model.QQLoginResult;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.model.manager.MessageManager;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.utils.AccountValidatorUtil;
import com.sunny.youyun.utils.GsonUtil;
import com.sunny.youyun.utils.JPushUtil;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.idling.EspressoIdlingResource;
import com.sunny.youyun.utils.share.TencentUtil;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.RichEditText;
import com.tencent.tauth.Tencent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Router(IntentRouter.LoginActivity)
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
    @BindView(R.id.img_qq_login)
    ImageView imgQqLogin;
    @BindView(R.id.img_we_chat_login)
    ImageView imgWeChatLogin;

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
    public void loginSuccess() {
        dismissDialog();
        RouterUtils.open(this, IntentRouter.MainActivity);
        onBackPressed();
        //初始化加载消息
        MessageManager.getInstance().init(UserInfoManager.getInstance()
                .getUserId());
        //登录成功以后设置本机的tag为用户的id
        JPushUtil.setTag(this, String.valueOf(UserInfoManager.getInstance()
                .getUserInfo()
                .getId()));
        EspressoIdlingResource.getInstance()
                .decrement();
    }

    @OnClick({R.id.tv_forget_pass, R.id.btn_login, R.id.btn_register, R.id.img_qq_login, R.id.img_we_chat_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_qq_login:
                showLoading();
                Observable.create((ObservableOnSubscribe<QQLoginResult>) e ->
                        //调用QQ登录接口，耗时，顾放在异步线程处理
                        TencentUtil.getInstance(LoginActivity.this)
                                .login(new BaseUiListener() {
                                    @Override
                                    public void onComplete(Object o) {
                                        QQLoginResult result = GsonUtil.getInstance()
                                                .fromJson(GsonUtil.getInstance().toJson(o), QQLoginResult.class);
                                        Logger.i("stringResult: " + result);
                                        YouyunAPI.updateQQLoginResult(result);
                                        e.onNext(result);
                                    }
                                }))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            showLoading();
                            getMPresenter().qqLogin(result);
                        });

                break;
            case R.id.img_we_chat_login:
                showTip("待开放");
                break;
            case R.id.tv_forget_pass:
                RouterUtils.open(this, IntentRouter.ForgetPassActivity);
                break;
            case R.id.btn_login:
                EspressoIdlingResource.getInstance()
                        .increment();
                String phone = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (phone.equals("") || password.equals("")) {
                    Toast.makeText(this, getString(R.string.please_complete_info), Toast.LENGTH_SHORT).show();
                } else if (AccountValidatorUtil.isMobile(phone)) {
                    showLoading();
                    getMPresenter().login(phone, password);
                } else {
                    Toast.makeText(this, getString(R.string.please_input_correct_phone_number), Toast.LENGTH_SHORT).show();
                }
//                TencentUtil.getInstance(this)
//                        .loginOut();
                break;
            case R.id.btn_register:
                RouterUtils.open(this, IntentRouter.RegisterActivity);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dismissDialog();
        Tencent.onActivityResultData(requestCode, resultCode, data,
                new BaseUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        QQLoginResult result = GsonUtil.getInstance()
                                .fromJson(GsonUtil.getInstance().toJson(o), QQLoginResult.class);
                        Logger.i("result2: " + result);

                    }
                });
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getInstance()
                .getIdlingResource();
    }
}
