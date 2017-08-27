package com.sunny.youyun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.sunny.youyun.R;
import com.tencent.tauth.Tencent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TencentTest extends AppCompatActivity {

    @BindView(R.id.btn_login)
    Button btnLogin;
    private Tencent mTencent;
    private BaseUiListener listener;
    private String APP_ID = "1105716704";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tencent_test);
        ButterKnife.bind(this);

        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
        listener = new BaseUiListener();
        initViews();
    }

    public void login() {
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", listener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
    }

    private void initViews() {

    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        login();
    }
}
