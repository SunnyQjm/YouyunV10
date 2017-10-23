package com.sunny.youyun.activity.file_detail_online;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.App;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_detail_online.adapter.CommentAdapter;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.internet.download.FileDownloader;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.ShareContent;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.model.data_item.Comment;
import com.sunny.youyun.model.result.ScanResult;
import com.sunny.youyun.utils.EasyPermission;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.utils.GlideOptions;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.GsonUtil;
import com.sunny.youyun.utils.InputMethodUtil;
import com.sunny.youyun.utils.MyClickText;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.utils.RxPermissionUtil;
import com.sunny.youyun.utils.Tool;
import com.sunny.youyun.utils.WindowUtil;
import com.sunny.youyun.utils.bus.ObjectPool;
import com.sunny.youyun.utils.idling.EspressoIdlingResource;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.EasyDialog;
import com.sunny.youyun.views.RichText;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;
import com.sunny.youyun.views.youyun_dialog.share.ShareDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(value = {IntentRouter.FileDetailOnlineActivity + "/:uuid",
        IntentRouter.FileDetailOnlineActivity + "/:fileId" + "/:identifyCode"},
        stringParams = "uuid", intParams = "fileId")
public class FileDetailOnlineActivity extends MVPBaseActivity<FileDetailOnlinePresenter> implements FileDetailOnlineContract.View, EasyRefreshLayout.OnRefreshListener, EasyRefreshLayout.OnLoadListener, View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.refreshLayout)
    EasyRefreshLayout refreshLayout;
    @BindView(R.id.et_content)
    EditText etCommentContent;
    @BindView(R.id.btn_send)
    Button btnSend;

    private ImageView imgIcon;
    private TextView tvFileNameSize;
    private TextView tvCode;
    private RichText rtViewNum;
    private RichText rtLikeNum;
    private RichText rtDownNum;
    private Button btnLookComment;
    private Button btnDownloadNow;
    private TextView tvDescription;
    private ImageView imgQRCode;

    private View header;
    private CommentAdapter adapter;
    @BindView(R.id.recyclerView_comment)
    RecyclerView recyclerViewComment;

    private InternetFile originalInternetFile = null;
    private InternetFile internetFile = null;
    private final List<InternetFile> mFileList = App.mList_DownloadRecord;
    private ShareDialog shareDialog = null;


    @Override
    protected void onStart() {
        super.onStart();
    }

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
                                if (aBoolean)
                                    showShareDialog(view);
                            });
                    return;
                }
                showShareDialog(view);
            }
        });
        createHeader();


        adapter = new CommentAdapter(mPresenter.getCommentList());
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewComment.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerViewComment);
        adapter.addHeaderView(header);
        adapter.setOnItemChildClickListener(this);

        String uuid = getIntent().getStringExtra("uuid");
        int fileId = getIntent().getIntExtra("fileId", -1);
        String identifyCode = getIntent().getStringExtra("identifyCode");
        if (fileId < 0) {
            internetFile = originalInternetFile = ObjectPool.getInstance()
                    .get(uuid, InternetFile.empty());
            //浏览量+1
            originalInternetFile.setLookNum(originalInternetFile.getLookNum() + 1);
            fillData();

            mPresenter.getFileInfo(internetFile.getIdentifyCode());
            mPresenter.getComments(internetFile.getId(), true);
        } else {
            mPresenter.getFileInfo(identifyCode);
            mPresenter.getComments(fileId, true);
        }
    }

    /**
     * 填充数据
     */
    private void fillData() {
        if (internetFile.getUser() == null)
            tvDescription.setText(internetFile.getDescription());
        else {
            SpannableString spannableString = buildDescription();
            tvDescription.setText(spannableString);
        }
        if (!internetFile.isCanStar()) {
            rtLikeNum.setDrawableRes(R.drawable.icon_zan_selected);
        } else {
            rtLikeNum.setDrawableRes(R.drawable.icon_zan);
        }
        GlideUtils.setImage(this, imgIcon, internetFile);
        tvCode.setText(String.format("%s: %s", getString(R.string.code), internetFile.getIdentifyCode()));
        tvFileNameSize.setText(String.format("%s(%s)", internetFile.getName(), Tool.convertToSize(internetFile.getSize())));
        rtViewNum.setText(String.valueOf(internetFile.getLookNum()));
        rtLikeNum.setText(String.valueOf(internetFile.getStar()));
        rtDownNum.setText(String.valueOf(internetFile.getDownloadCount()));
    }

    private SpannableString buildDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        String uploader = getString(R.string.uploader) + " : ";
        String result = stringBuilder.append(uploader)
                .append(internetFile.getUser().getUsername())
                .append("\n")
                .append(internetFile.getDescription())
                .toString();
        SpannableString spannableString = new SpannableString(result);
        spannableString.setSpan(new MyClickText(this, v -> {
                    RouterUtils.openToUser(this, internetFile.getUserId());
                }),
                uploader.length(), internetFile.getUser().getUsername().length() + uploader.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    private void createHeader() {
        header = LayoutInflater.from(this).inflate(R.layout.file_detail_online_header, null, false);
        imgIcon = (ImageView) header.findViewById(R.id.img_icon);
        tvFileNameSize = (TextView) header.findViewById(R.id.tv_file_name_size);
        tvCode = (TextView) header.findViewById(R.id.tv_code);
        rtViewNum = (RichText) header.findViewById(R.id.rt_view_count);
        rtLikeNum = (RichText) header.findViewById(R.id.rt_like_count);
        rtDownNum = (RichText) header.findViewById(R.id.rt_down_count);
        btnLookComment = (Button) header.findViewById(R.id.btn_look_comment);
        btnDownloadNow = (Button) header.findViewById(R.id.btn_download_now);
        tvDescription = (TextView) header.findViewById(R.id.tv_detail_info_content);
        imgQRCode = (ImageView) header.findViewById(R.id.img_qr_code);
        tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        tvDescription.setHighlightColor(Color.TRANSPARENT);
        imgIcon.setOnClickListener(this);
        tvFileNameSize.setOnClickListener(this);
        tvCode.setOnClickListener(this);
        rtViewNum.setOnClickListener(this);
        rtLikeNum.setOnClickListener(this);
        rtDownNum.setOnClickListener(this);
        btnLookComment.setOnClickListener(this);
        btnDownloadNow.setOnClickListener(this);
        imgQRCode.setOnClickListener(this);
    }

    private void showShareDialog(View parent) {
        if (shareDialog == null)
            shareDialog = new ShareDialog(FileDetailOnlineActivity.this,
                    new ShareContent.Builder()
                            .shareAppName(getString(R.string.app_name))
                            .shareImageUrl(FileUtils.getLogoPath())
                            .shareSummary(internetFile.getDescription() == null || internetFile.getDescription().equals("") ?
                                    getString(R.string.share_from_youyun) : internetFile.getDescription())
                            .shareTitle(internetFile.getName())
                            .fileId(internetFile.getId())
                            .canStore(internetFile.isCanStore())
                            .identifyCode(internetFile.getIdentifyCode())
                            .shareUrl(ApiInfo.BASE_DOWNLOAD_URL + internetFile.getIdentifyCode())
                            .build(), new ShareDialog.OnCollectionListener() {
                @Override
                public void onCollectionSuccess() {
                    if (internetFile.isCanStore()) {
                        internetFile.setCanStore(false);
                        originalInternetFile.setCanStore(false);
                        shareDialog.setCollectName(getString(R.string.cancel_collection));
                        showSuccess(getString(R.string.collect_success));
                    } else {
                        internetFile.setCanStore(true);
                        originalInternetFile.setCanStore(true);
                        shareDialog.setCollectName(getString(R.string.collection));
                        showSuccess(getString(R.string.cancel_collect_success));
                    }
                    fillData();
                }

                @Override
                public void onCollectionFailed() {
                }
            });
        shareDialog.show(parent, null);
        shareDialog.setOnDismissListener(() -> WindowUtil.changeWindowAlpha(this, false));
        WindowUtil.changeWindowAlpha(this, true);
    }

    @Override
    protected FileDetailOnlinePresenter onCreatePresenter() {
        return new FileDetailOnlinePresenter(this);
    }


    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void download(String identifyCode) {
        int position;
        internetFile.setFileTAG(InternetFile.TAG_DOWNLOAD);
        synchronized (mFileList) {
            position = mFileList.size();
            mFileList.add(internetFile);
        }
        internetFile.setPath(FileUtils.getDownloadPath() + internetFile.getName());

        FileDownloader.getInstance()
                .download(ApiInfo.BaseUrl + ApiInfo.DOWNLOAD + identifyCode, internetFile.getName(), position);
        if (internetFile != originalInternetFile)
            internetFile.setDownloadCount(internetFile.getDownloadCount() + 1);
        originalInternetFile.setDownloadCount(originalInternetFile.getDownloadCount() + 1);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {

    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoad() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rt_view_count:
                break;
            case R.id.rt_like_count:
                mPresenter.star(internetFile.getId());
                break;
            case R.id.rt_down_count:
                break;
            case R.id.btn_look_comment:
                break;
            case R.id.img_qr_code:  //显示二维码
                //将圆形头像放在二维码中间
                EasyPermission.checkAndRequestREAD_WRITE_EXTENAL(this,
                        new EasyPermission.OnPermissionRequestListener() {
                            @Override
                            public void success() {
                                System.out.println("display");
                                displayQRCode();
                            }

                            @Override
                            public void fail() {

                            }
                        });
                break;
            case R.id.btn_download_now:
                if (internetFile == null)
                    return;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    EasyPermission.checkAndRequestREAD_WRITE_EXTENAL(this, new EasyPermission.OnPermissionRequestListener() {
                        @Override
                        public void success() {
                            if (ActivityCompat.checkSelfPermission(FileDetailOnlineActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            download(internetFile.getIdentifyCode());
                        }

                        @Override
                        public void fail() {

                        }
                    });
                    return;
                }

                download(internetFile.getIdentifyCode());
                finish();
                break;
        }
    }

    private void displayQRCode() {
        Glide.with(this)
                .asBitmap()
                .apply(GlideOptions.getInstance()
                        .getAvatarOptions())
                .load(internetFile.getUser() == null ? R.drawable.icon_logo_round :
                        internetFile.getUser().getAvatar())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        ScanResult scanResult = new ScanResult.Builder()
                                .data(String.valueOf(internetFile.getId()))
                                .data2(internetFile.getIdentifyCode())
                                .type(ScanResult.TYPE_FILE)
                                .build();
                        showQrDialog(GsonUtil.bean2Json(scanResult))
                                .setCenterIcon(resource);
                    }
                });
    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        //TODO 发表评论
        if (YouyunAPI.isIsLogin()) {
            EspressoIdlingResource.getInstance()
                    .increment();
            mPresenter.addComment(internetFile.getId(), etCommentContent.getText().toString());
            etCommentContent.setText("");
            InputMethodUtil.change(this);
        } else {
            EasyDialog.showLogin(this);
        }
    }

    @Override
    public void commentSuccess() {
        mPresenter.getComments(internetFile.getId(), false);
    }

    @Override
    public void getCommentsSuccess(boolean isFirst) {

        if (adapter == null)
            return;
        if (!isFirst) {
            adapter.notifyDataSetChanged();
            //滚动到最后一条，因为有头布局，所以position要加1
            recyclerViewComment.scrollToPosition(mPresenter.getCommentList().size());
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getFileInfoSuccess(InternetFile internetFile) {
        this.internetFile = internetFile;
        fillData();
    }

    @Override
    public void starSuccess() {

        //点赞成功后直接
        if (internetFile.isCanStar()) {
            internetFile.setCanStar(false);
            originalInternetFile.setCanStar(false);
            internetFile.setStar(internetFile.getStar() + 1);
            originalInternetFile.setStar(originalInternetFile.getStar() + 1);
        } else {
            internetFile.setCanStar(true);
            originalInternetFile.setCanStar(true);
            internetFile.setStar(internetFile.getStar() - 1);
            originalInternetFile.setStar(originalInternetFile.getStar() - 1);
        }
        fillData();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        System.out.println("click");
        if (view.getId() == R.id.img_icon) {        //头像被点击
            System.out.println("cli");
            Comment comment = (Comment) adapter.getItem(position);
            if (comment == null)
                return;
            RouterUtils.open(this, IntentRouter.PersonInfoActivity,
                    String.valueOf(comment.getUserId()));
        }
    }


}
