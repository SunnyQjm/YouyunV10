package com.sunny.youyun.activity.file_detail_off_line;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.internet.download.FileDownloader;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.ShareContent;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.RxPermissionUtil;
import com.sunny.youyun.utils.Tool;
import com.sunny.youyun.utils.UUIDUtil;
import com.sunny.youyun.utils.WindowUtil;
import com.sunny.youyun.utils.bus.ObjectPool;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.youyun_dialog.share.ShareDialog;
import com.sunny.youyun.views.youyun_dialog.tip.OnYouyunTipDialogClickListener;
import com.sunny.youyun.views.youyun_dialog.tip.YouyunTipDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(value = IndexRouter.FileDetailOffLineActivity + "/:uuid/:position", stringParams = "uuid",
        intParams = "position")
public class FileDetailOffLineActivity extends MVPBaseActivity<FileDetailOffLinePresenter>
        implements FileDetailOffLineContract.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.tv_file_name_size)
    TextView tvFileNameSize;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.btn_detail_file)
    Button btnDetailFile;
    @BindView(R.id.btn_open_file)
    Button btnOpenFile;

    private InternetFile internetFile = null;
    private YouyunTipDialog youyunTipDialog = null;
    private int position;
    private ShareDialog shareDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail_off_line);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.file_detail));
        easyBar.setRightIcon(R.drawable.icon_share);
        easyBar.setRightIconVisible();
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(final View view) {
                //TODO add share operator here
                if (ActivityCompat.checkSelfPermission(FileDetailOffLineActivity.this,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(FileDetailOffLineActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    RxPermissionUtil.getInstance(FileDetailOffLineActivity.this)
                            .request(Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(aBoolean -> {
                                if (aBoolean)
                                    showShareDialog(view);
                            });
                    return;
                }
                showShareDialog(view);
            }
        });

        Intent intent = getIntent();
        String uuid = intent.getStringExtra("uuid");
        position = intent.getIntExtra("position", 0);
        if (uuid == null)
            return;
        internetFile = ObjectPool.getInstance().get(uuid, InternetFile.empty());
        if (internetFile == null)
            return;

        GlideUtils.setImage(this, imgIcon, internetFile);
        tvCode.setText(String.format("%s: %s", getString(R.string.code), internetFile.getIdentifyCode()));
        tvFileNameSize.setText(String.format("%s(%s)", internetFile.getName(), Tool.convertToSize(internetFile.getSize())));
    }

    @RequiresPermission(allOf = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void showShareDialog(View parent) {
        if (shareDialog == null)
            shareDialog = new ShareDialog(FileDetailOffLineActivity.this,
                    new ShareContent.Builder()
                            .shareAppName(getString(R.string.app_name))
                            .shareImageUrl("http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif")
                            .shareTitle(internetFile.getName())
                            .shareSummary("")
                            .identifyCode(internetFile.getIdentifyCode())
                            .shareUrl(ApiInfo.BASE_DOWNLOAD_URL + internetFile.getIdentifyCode())
                            .build());
        shareDialog.show(parent, null);
        shareDialog.setOnDismissListener(() -> WindowUtil.changeWindowAlpha(this, 1.0f));
        WindowUtil.changeWindowAlpha(this, 0.7f);
    }

    @Override
    protected FileDetailOffLinePresenter onCreatePresenter() {
        return new FileDetailOffLinePresenter(this);
    }

    @OnClick({R.id.btn_detail_file, R.id.btn_open_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_detail_file:
                mPresenter.getFileInfo(internetFile.getIdentifyCode());
                break;
            case R.id.btn_open_file:
                //TODO open current internetFile
                //if the internetFile not exist, tip
                if (internetFile == null)
                    return;
                File f = new File(internetFile.getPath());
                if (!f.exists()) {
                    showFileNotExistDialog();
                    return;
                }
                openFile();
                break;
        }
    }

    private void showFileNotExistDialog() {
        if (youyunTipDialog == null)
            youyunTipDialog = YouyunTipDialog.newInstance(R.drawable.icon_setting_cancel,
                    "文件不见啦！是否重新下载?", new OnYouyunTipDialogClickListener() {
                        @Override
                        public void onCancelClick() {
                            youyunTipDialog.dismiss();
                        }

                        @Override
                        public void onSureClick() {
                            if (ActivityCompat.checkSelfPermission(FileDetailOffLineActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                RxPermissionUtil.getInstance(FileDetailOffLineActivity.this)
                                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .subscribe(aBoolean -> {
                                            if (aBoolean)
                                                download(internetFile, position);
                                            finish();
                                        });
                                return;
                            }
                            download(internetFile, position);
                        }
                    });
        youyunTipDialog.show(getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void download(InternetFile internetFile, int position) {
        internetFile.setFileTAG(InternetFile.TAG_DOWNLOAD);
        internetFile.setPath(FileUtils.getDownloadPath() + internetFile.getName());
        FileDownloader.getInstance()
                .download(ApiInfo.BaseUrl + ApiInfo.DOWNLOAD + internetFile.getIdentifyCode(),
                        internetFile.getName(), position);
        finish();
    }

    private void openFile() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissionUtil
                    .getInstance(this)
                    .request(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(aBoolean -> {
                        System.out.println("获取：" + aBoolean);
                        if (aBoolean)
                            FileUtils.openFile(this, internetFile.getPath());
                    });
        } else {
            FileUtils.openFile(this, internetFile.getPath());
        }
    }

    @Override
    public void showDetail(InternetFile internetFile) {
        String uuid = UUIDUtil.getUUID();
        ObjectPool.getInstance()
                .put(uuid, internetFile);
        RouterUtils.open(this, IndexRouter.FileDetailOnlineActivity, uuid);
        finish();
    }
}
