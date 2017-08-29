package com.sunny.youyun.activity.scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.scan.config.ScanConfig;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.utils.RxPermissionUtil;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.QRScan.qrcode.core.QRCodeView;

import butterknife.BindView;
import butterknife.ButterKnife;

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
            // fix get permission in Xiaomi device under 6.0
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

    private void initView() {
        easyBar.setTitle("扫一扫");
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            getCameraPermission();
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
                        Logger.i("permissions", Manifest.permission.CAMERA + ": 获取成功");
                    } else {
                        Logger.i("permissions", Manifest.permission.CAMERA + ": 获取失败");
                    }
                });
    }
}
