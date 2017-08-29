package com.sunny.youyun.wifidirect.activity.single_trans.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.githang.statusbar.StatusBarCompat;
import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.WifiDirectBaseActivity;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.RichText;
import com.sunny.youyun.wifidirect.activity.single_trans.receiver.ReceiverActivity;
import com.sunny.youyun.wifidirect.activity.single_trans.send.SenderActivity;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IndexRouter.SingleTransMainActivity)
public class SingleTransMainActivity extends WifiDirectBaseActivity<SingleTransMainPresenter>
        implements SingleTranMainContract.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.btn_i_want_receive_file)
    RichText btnIWantReceiveFile;
    @BindView(R.id.btn_i_want_send_file)
    RichText btnIWantSendFile;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_trans_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.blue, null));
        } else {
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.blue));
        }
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle("一对一传输");
        easyBar.setRightIcon(R.drawable.icon_history);
        easyBar.setRightIconVisible();
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {
                //TODO here to go to history
            }
        });
        WifiDirectManager.getInstance().disConnect();
    }


    @Override
    protected SingleTransMainPresenter onCreatePresenter() {
        return new SingleTransMainPresenter(this);
    }

    @OnClick({R.id.btn_i_want_receive_file, R.id.btn_i_want_send_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_i_want_receive_file:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    RouterUtils.openWithAnimation(this ,new Intent(this, ReceiverActivity.class));
                } else {
                    RouterUtils.open(this, IndexRouter.ReceiverActivity);
                }
                break;
            case R.id.btn_i_want_send_file:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    RouterUtils.openWithAnimation(this, new Intent(this, SenderActivity.class));
                } else {
                    RouterUtils.open(this, IndexRouter.SenderActivity);
                }
                break;
        }
    }
}
