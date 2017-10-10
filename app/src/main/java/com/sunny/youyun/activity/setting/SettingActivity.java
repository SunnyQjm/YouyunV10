package com.sunny.youyun.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.model.EasyYouyunAPIManager;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.LineMenuItem;
import com.sunny.youyun.views.LineMenuSwitch;
import com.sunny.youyun.views.youyun_dialog.tip.OnYouyunTipDialogClickListener;
import com.sunny.youyun.views.youyun_dialog.tip.YouyunTipDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IntentRouter.SettingActivity)
public class SettingActivity extends MVPBaseActivity<SettingPresenter> implements SettingContract.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.setting_is_tran_on_wifi_state)
    LineMenuSwitch settingIsTranOnWifiState;
    @BindView(R.id.setting_is_receive_notify)
    LineMenuSwitch settingIsReceiveNotify;
    @BindView(R.id.setting_is_update_auto_on_wifi_state)
    LineMenuSwitch settingIsUpdateAutoOnWifiState;
    @BindView(R.id.setting_clear_cache)
    LineMenuItem settingClearCache;
    @BindView(R.id.setting_about_youyun)
    LineMenuItem settingAboutYouyun;
    @BindView(R.id.exit)
    TextView exit;

    private YouyunTipDialog tipDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.setting));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });

        settingIsReceiveNotify.setChecked(YouyunAPI.isIsAcceptNotify());
        settingIsReceiveNotify.setOnCheckedChangeListener((buttonView, isChecked) -> {
            YouyunAPI.updateIsAcceptNotify(isChecked);
        });

        settingIsTranOnWifiState.setChecked(YouyunAPI.isIsOnlyWifiDownload());
        settingIsTranOnWifiState.setOnCheckedChangeListener((buttonView, isChecked) -> {
           YouyunAPI.updateIsOnlyWifiDownload(isChecked);
        });

        settingIsUpdateAutoOnWifiState.setChecked(YouyunAPI.isIsWifiAutoUpdate());
        settingIsUpdateAutoOnWifiState.setOnCheckedChangeListener((buttonView, isChecked) -> {
            YouyunAPI.updateIsWifiAutoUpdate(isChecked);
        });

    }

    @Override
    protected SettingPresenter onCreatePresenter() {
        return new SettingPresenter(this);
    }

    /**
     * 清理缓存
     */
    @OnClick(R.id.setting_clear_cache)
    public void onSettingClearCacheClicked() {
//        mPresenter.logout();
    }

    /**
     * 关于
     */
    @OnClick(R.id.setting_about_youyun)
    public void onSettingAboutYouyunClicked() {
        RouterUtils.open(this, IntentRouter.AboutYouyunActivity);
    }

    /**
     * 退出
     */
    @OnClick(R.id.exit)
    public void onExitClicked() {
        if (YouyunAPI.isIsLogin()) {
            showExitTipDialog();
        } else {
            exit();
        }
    }

    private void showExitTipDialog() {
        if (tipDialog == null)
            tipDialog = YouyunTipDialog.newInstance(R.drawable.icon_tip,
                    getString(R.string.user_exit_tip), new OnYouyunTipDialogClickListener() {
                        @Override
                        public void onCancelClick() {
                            tipDialog.dismiss();
                        }

                        @Override
                        public void onSureClick() {
                            exit();
                        }
                    });
        tipDialog.show(getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    private void exit() {
        EasyYouyunAPIManager.logout(this);
        finish();
    }
}
