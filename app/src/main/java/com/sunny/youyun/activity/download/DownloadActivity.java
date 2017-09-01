package com.sunny.youyun.activity.download;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.UUIDUtil;
import com.sunny.youyun.utils.bus.ObjectPool;
import com.sunny.youyun.views.EasyBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IntentRouter.DownloadActivity)
public class DownloadActivity extends MVPBaseActivity<DownloadPresenter> implements DownloadContract.View {


    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_get)
    Button btnGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.downloadFile));
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
    protected DownloadPresenter onCreatePresenter() {
        return new DownloadPresenter(this);
    }

    @OnClick(R.id.btn_get)
    public void onViewClicked() {
        String code = etCode.getText().toString();
        if (code.equals("") || code.length() != 6){
            showTip(getString(R.string.please_input_correct_identify_code));
            return;
        }
        mPresenter.getFileInfo(code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showDetail(InternetFile data) {
        String uuid = UUIDUtil.getUUID();
        ObjectPool.getInstance()
                .put(uuid, data);
        RouterUtils.open(this, IntentRouter.FileDetailOnlineActivity, uuid);
        finish();
    }
}
