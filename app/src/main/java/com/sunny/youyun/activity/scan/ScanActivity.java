package com.sunny.youyun.activity.scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.scan.config.ScanConfig;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.utils.RxPermissionUtil;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.QRScan.qrcode.core.QRCodeView;

import butterknife.BindView;
import butterknife.ButterKnife;

@Router(IntentRouter.ScanActivity)
public class ScanActivity extends MVPBaseActivity<ScanPresenter> implements ScanContract.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.zxingview)
    QRCodeView mQRCodeView;

    private QRCodeView.Delegate delegate = new QRCodeView.Delegate() {
        @Override
        public void onScanQRCodeSuccess(String result) {
            mQRCodeView.stopCamera();
            Intent intent = new Intent();
            intent.putExtra(ScanConfig.SCAN_RESULT, result);
            setResult(0, intent);
            finish();
        }

        @Override
        public void onScanQRCodeOpenCameraError(Throwable throwable) {
            Logger.e(throwable, "打开摄像头失败");
            // fix getCount permission in Xiaomi device under 6.0
            if (Build.MANUFACTURER.equals("Xiaomi")
                    && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                mQRCodeView.post(() -> {
                    mQRCodeView.stopSpotAndHiddenRect();
                    mQRCodeView.stopCamera();
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQRCodeView != null) {
            mQRCodeView.stopSpot();
            mQRCodeView.stopCamera();
        }
    }

    private void initView() {
        easyBar.setTitle("扫一扫");
        easyBar.setLeftIconVisible();
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });

        mQRCodeView.setDelegate(delegate);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            getCameraPermission();
            return;
        }
        mQRCodeView.startSpot();
    }

    @Override
    protected ScanPresenter onCreatePresenter() {
        return new ScanPresenter(this);
    }

    public void getCameraPermission() {
        RxPermissionUtil.getInstance(this)
                .request(android.Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mQRCodeView.startSpot();
                        Logger.i("permissions", Manifest.permission.CAMERA + ": 获取成功");
                    } else {
                        Logger.i("permissions", Manifest.permission.CAMERA + ": 获取失败");
                    }
                });
    }
}
