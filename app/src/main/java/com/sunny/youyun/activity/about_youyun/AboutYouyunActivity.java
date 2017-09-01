package com.sunny.youyun.activity.about_youyun;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.LineMenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IntentRouter.AboutYouyunActivity)
public class AboutYouyunActivity extends MVPBaseActivity<AboutYouyunPresenter> implements AboutYouyunContract.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.li_about_youyun_version_update)
    LineMenuItem liAboutYouyunVersionUpdate;
    @BindView(R.id.li_about_youyun_user_protocol)
    LineMenuItem liAboutYouyunUserProtocol;
    @BindView(R.id.li_about_youyun_about_us)
    LineMenuItem liAboutYouyunAboutUs;
    @BindView(R.id.li_about_youyun_comment)
    LineMenuItem liAboutYouyunComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_youyun);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.about_youyun));
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
    protected AboutYouyunPresenter onCreatePresenter() {
        return new AboutYouyunPresenter(this);
    }

    /**
     * 版本更新
     */
    @OnClick(R.id.li_about_youyun_version_update)
    public void onLiAboutYouyunVersionUpdateClicked() {
    }

    /**
     * 用户协议
     */
    @OnClick(R.id.li_about_youyun_user_protocol)
    public void onLiAboutYouyunUserProtocolClicked() {
        Toast.makeText(this, "用户协议", Toast.LENGTH_SHORT).show();
    }

    /**
     * 关于我们
     */
    @OnClick(R.id.li_about_youyun_about_us)
    public void onLiAboutYouyunAboutUsClicked() {
    }

    /**
     * 去评论
     */
    @OnClick(R.id.li_about_youyun_comment)
    public void onLiAboutYouyunCommentClicked() {
    }
}
