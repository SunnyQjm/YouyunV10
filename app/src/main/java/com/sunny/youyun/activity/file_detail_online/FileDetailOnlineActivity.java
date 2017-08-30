package com.sunny.youyun.activity.file_detail_online;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.App;
import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_detail_off_line.FileDetailOffLineActivity;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.internet.download.FileDownloader;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.ShareContent;
import com.sunny.youyun.model.User;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.RxPermissionUtil;
import com.sunny.youyun.utils.Tool;
import com.sunny.youyun.utils.WindowUtil;
import com.sunny.youyun.utils.bus.ObjectPool;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.RichText;
import com.sunny.youyun.views.youyun_dialog.share.ShareDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(value = IndexRouter.FileDetailOnlineActivity + "/:uuid", stringParams = "uuid")
public class FileDetailOnlineActivity extends MVPBaseActivity<FileDetailOnlinePresenter> implements FileDetailOnlineContract.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.img_avatar)
    ImageView imgAvatar;
    @BindView(R.id.tv_user_s_share)
    TextView tvUserSShare;
    @BindView(R.id.btn_concern)
    Button btnConcern;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.tv_file_name_size)
    TextView tvFileNameSize;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.rt_view_num)
    RichText rtViewNum;
    @BindView(R.id.rt_like_num)
    RichText rtLikeNum;
    @BindView(R.id.rt_down_num)
    RichText rtDownNum;
    @BindView(R.id.btn_look_comment)
    Button btnLookComment;
    @BindView(R.id.btn_download_now)
    Button btnDownloadNow;
    @BindView(R.id.cl_user_info)
    ConstraintLayout clUserInfo;

    private InternetFile internetFile = null;
    private final List<InternetFile> mList = App.mList_DownloadRecord;
    private ShareDialog shareDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail_online);
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
            public void onRightIconClick(View view) {
                //TODO add share operator here
                if (ActivityCompat.checkSelfPermission(FileDetailOnlineActivity.this,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(FileDetailOnlineActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    RxPermissionUtil.getInstance(FileDetailOnlineActivity.this)
                            .request(Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(aBoolean -> {
                                if(aBoolean)
                                    showShareDialog(view);
                            });
                    return;
                }
                showShareDialog(view);
            }
        });
        String uuid = getIntent().getStringExtra("uuid");
        internetFile = ObjectPool.getInstance().get(uuid, InternetFile.empty());
        if (internetFile.getUser() == null)
            clUserInfo.setVisibility(View.GONE);
        else {
            User user = internetFile.getUser();
            GlideUtils.loadUrl(this, imgAvatar, user.getAvatar());
            tvUserSShare.setText(String.format("%s%s", user.getUsername(), getString(R.string.xxs_share)));
        }
        GlideUtils.setImage(this, imgIcon, internetFile);
        tvCode.setText(String.format("%s: %s", getString(R.string.code), internetFile.getIdentifyCode()));
        tvFileNameSize.setText(String.format("%s(%s)", internetFile.getName(), Tool.convertToSize(internetFile.getSize())));
        rtViewNum.setText(String.valueOf(internetFile.getLookNum()));
        rtLikeNum.setText(String.valueOf(internetFile.getStar()));
        rtDownNum.setText(String.valueOf(internetFile.getDownloadCount()));
    }

    private void showShareDialog(View parent) {
        if (shareDialog == null)
            shareDialog = new ShareDialog(FileDetailOnlineActivity.this,
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
    protected FileDetailOnlinePresenter onCreatePresenter() {
        return new FileDetailOnlinePresenter(this);
    }

    @OnClick({R.id.img_avatar, R.id.btn_concern, R.id.rt_view_num, R.id.rt_like_num, R.id.rt_down_num, R.id.btn_look_comment, R.id.btn_download_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_avatar:
                break;
            case R.id.btn_concern:
                break;
            case R.id.rt_view_num:
                break;
            case R.id.rt_like_num:
                rtLikeNum.setDrawableRes(R.drawable.icon_zan_selected);
                break;
            case R.id.rt_down_num:
                break;
            case R.id.btn_look_comment:
                break;
            case R.id.btn_download_now:
                if (internetFile == null)
                    return;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    RxPermissionUtil.getInstance(this)
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(aBoolean -> {
                                if (aBoolean)
                                    download(internetFile.getIdentifyCode());
                                finish();
                            });
                    return;
                }
                download(internetFile.getIdentifyCode());
                finish();
                break;
        }
    }

    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void download(String identifyCode) {
        int position;
        internetFile.setFileTAG(InternetFile.TAG_DOWNLOAD);
        synchronized (mList) {
            position = mList.size();
            mList.add(internetFile);
        }
        internetFile.setPath(FileUtils.getDownloadPath() + internetFile.getName());
        FileDownloader.getInstance()
                .download(ApiInfo.BaseUrl + ApiInfo.DOWNLOAD + identifyCode, internetFile.getName(), position);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
