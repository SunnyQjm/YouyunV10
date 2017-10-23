package com.sunny.youyun.activity.share_success;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.databinding.ActivityShareSuccessBinding;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.ShareContent;
import com.sunny.youyun.model.data_item.ShareSuccess;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.utils.GlideOptions;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.QRCodeUtil;
import com.sunny.youyun.utils.bus.ObjectPool;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.youyun_dialog.share.ShareDialog;
import com.tencent.connect.share.QQShare;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IntentRouter.ShareSuccessActivity + "/:uuid")
public class ShareSuccessActivity extends MVPBaseActivity<ShareSuccessPresenter>
        implements ShareSuccessContract.View {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.img_qr)
    ImageView imgQr;
    @BindView(R.id.tv_file_name_size)
    TextView tvFileNameSize;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.btn_share_qr)
    Button btnShareQr;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.btn_share_link)
    Button btnShareLink;

    private ActivityShareSuccessBinding binding = null;
    private ShareSuccess shareSuccess = null;
    private static final String QR_PATH = FileUtils.getShareQrPath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_share_success
        );

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.share_success));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });

        String uuid = getIntent().getStringExtra("uuid");
        shareSuccess = ObjectPool.getInstance()
                .get(uuid, ShareSuccess.empty());
        binding.setShare(shareSuccess);
    }

    @BindingAdapter({"fileName", "filePath"})
    public static void loadIcon(ImageView view, String fileName,
                                String filePath) {
        GlideUtils.setImage(view.getContext(), view, fileName, filePath);
    }

    @BindingAdapter({"downloadLink"})
    public static void loadQr(ImageView view, String downloadLink) {
        //将圆形头像放在二维码中间
        Glide.with(view.getContext())
                .asBitmap()
                .apply(GlideOptions.getInstance()
                        .getAvatarOptions())
                .load(UserInfoManager.getInstance().getUserInfo().getAvatar())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        QRCodeUtil.createQRImage(downloadLink, view.getMeasuredWidth(),
                                view.getMeasuredHeight(),
                                resource, QR_PATH);
                        Bitmap bitmap1 = BitmapFactory.decodeFile(QR_PATH);
                        view.setImageBitmap(bitmap1);
                    }
                });
    }

    @Override
    protected ShareSuccessPresenter onCreatePresenter() {
        return new ShareSuccessPresenter(this);
    }


    @OnClick({R.id.btn_share_qr, R.id.btn_share_link})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_share_qr:
                InternetFile internetFile = shareSuccess.getFile().getFile();
                if (internetFile == null)
                    return;
                ShareDialog shareDialog = new ShareDialog(this, new ShareContent.Builder()
                        .shareType(QQShare.SHARE_TO_QQ_TYPE_IMAGE)
                        .shareAppName(getString(R.string.app_name))
                        .shareImageUrl(FileUtils.getShareQrPath())
                        .shareSummary(internetFile.getDescription() == null || internetFile.getDescription().equals("") ?
                                getString(R.string.share_from_youyun) : internetFile.getDescription())
                        .shareTitle(internetFile.getName())
                        .fileId(internetFile.getId())
                        .canStore(internetFile.isCanStore())
                        .identifyCode(internetFile.getIdentifyCode())
                        .shareUrl(shareSuccess.getDownloadLink())
                        .build(), ShareDialog.ShareType.SHARE_TO_QQ, ShareDialog.ShareType.SHARE_TO_QQ_ZONE,
                        ShareDialog.ShareType.SHARE_TO_WE_CHAT, ShareDialog.ShareType.SHARE_FRIEND_CIRCLE,
                        ShareDialog.ShareType.SHARE_COPY_LINK);
                shareDialog.show(view, null);
                break;
            case R.id.btn_share_link:
                internetFile = shareSuccess.getFile().getFile();
                if (internetFile == null)
                    return;
                shareDialog = new ShareDialog(this, new ShareContent.Builder()
                        .shareAppName(getString(R.string.app_name))
                        .shareImageUrl(FileUtils.getLogoPath())
                        .shareSummary(internetFile.getDescription() == null || internetFile.getDescription().equals("") ?
                                getString(R.string.share_from_youyun) : internetFile.getDescription())
                        .shareTitle(internetFile.getName())
                        .fileId(internetFile.getId())
                        .canStore(internetFile.isCanStore())
                        .identifyCode(internetFile.getIdentifyCode())
                        .shareUrl(shareSuccess.getDownloadLink())
                        .build(), ShareDialog.ShareType.SHARE_TO_QQ, ShareDialog.ShareType.SHARE_TO_QQ_ZONE,
                        ShareDialog.ShareType.SHARE_TO_WE_CHAT, ShareDialog.ShareType.SHARE_FRIEND_CIRCLE,
                        ShareDialog.ShareType.SHARE_COPY_LINK);
                shareDialog.show(view, null);
                break;
        }


    }
}
