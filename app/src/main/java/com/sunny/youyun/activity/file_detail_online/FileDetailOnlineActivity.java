package com.sunny.youyun.activity.file_detail_online;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.App;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_detail_online.adapter.CommentAdapter;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.internet.download.FileDownloader;
import com.sunny.youyun.model.Comment;
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
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;
import com.sunny.youyun.views.youyun_dialog.share.ShareDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Router(value = IntentRouter.FileDetailOnlineActivity + "/:uuid", stringParams = "uuid")
public class FileDetailOnlineActivity extends MVPBaseActivity<FileDetailOnlinePresenter> implements FileDetailOnlineContract.View, EasyRefreshLayout.OnRefreshListener, EasyRefreshLayout.OnLoadListener, View.OnClickListener {

    @BindView(R.id.easyBar)
    EasyBar easyBar;

    private ImageView imgIcon;
    private TextView tvFileNameSize;
    private TextView tvCode;
    private RichText rtViewNum;
    private RichText rtLikeNum;
    private RichText rtDownNum;
    private Button btnLookComment;
    private Button btnDownloadNow;

    private View header;
    private CommentAdapter adapter;
    private List<Comment> mCommentList;
    @BindView(R.id.recyclerView_comment)
    RecyclerView recyclerViewComment;
//    @BindView(R.id.refreshLayout)
//    EasyRefreshLayout refreshLayout;

    private InternetFile internetFile = null;
    private final List<InternetFile> mFileList = App.mList_DownloadRecord;
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
                                if (aBoolean)
                                    showShareDialog(view);
                            });
                    return;
                }
                showShareDialog(view);
            }
        });
        createHeader();

//        refreshLayout.setHeader(new ArrowRefreshHeader(R.layout.easy_refresh_header));
//        refreshLayout.setFooter(new EasyRefreshFooter(R.layout.easy_refresh_footer));
//        refreshLayout.setOnRefreshListener(this);
//        refreshLayout.setOnLoadListener(this);
        mCommentList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mCommentList.add(new Comment.Builder()
                    .comment("WOOW, WOOW, 好评！WOOW, WOOW, 好评！WOOW, WOOW, 好评！WOOW, WOOW, 好评！WOOW, WOOW, 好评！WOOW, WOOW, 好评！WOOW, WOOW, 好评！")
                    .commentDate(System.currentTimeMillis())
                    .user(new User.Builder()
                            .avatar("http://img4.imgtn.bdimg.com/it/u=2880820503,781549093&fm=27&gp=0.jpg")
                            .username("GaoSQ" + i)
                            .build())
                    .build());
        }
        adapter = new CommentAdapter(mCommentList);
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewComment.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerViewComment);
        adapter.addHeaderView(header);

        String uuid = getIntent().getStringExtra("uuid");
        internetFile = ObjectPool.getInstance().get(uuid, InternetFile.empty());
//        if (internetFile.getUser() == null)
//            clUserInfo.setVisibility(View.GONE);
//        else {
//            User user = internetFile.getUser();
//            GlideUtils.loadUrl(this, imgAvatar, user.getAvatar());
//            tvUserSShare.setText(String.format("%s%s", user.getUsername(), getString(R.string.xxs_share)));
//        }
        GlideUtils.setImage(this, imgIcon, internetFile);
        tvCode.setText(String.format("%s: %s", getString(R.string.code), internetFile.getIdentifyCode()));
        tvFileNameSize.setText(String.format("%s(%s)", internetFile.getName(), Tool.convertToSize(internetFile.getSize())));
        rtViewNum.setText(String.valueOf(internetFile.getLookNum()));
        rtLikeNum.setText(String.valueOf(internetFile.getStar()));
        rtDownNum.setText(String.valueOf(internetFile.getDownloadCount()));
    }

    private void createHeader() {
        header = LayoutInflater.from(this).inflate(R.layout.file_detail_online_header, null, false);
        imgIcon = (ImageView) header.findViewById(R.id.img_icon);
        tvFileNameSize = (TextView) header.findViewById(R.id.tv_file_name_size);
        tvCode = (TextView) header.findViewById(R.id.tv_code);
        rtViewNum = (RichText) header.findViewById(R.id.rt_view_num);
        rtLikeNum = (RichText) header.findViewById(R.id.rt_like_num);
        rtDownNum = (RichText) header.findViewById(R.id.rt_down_num);
        btnLookComment = (Button) header.findViewById(R.id.btn_look_comment);
        btnDownloadNow = (Button) header.findViewById(R.id.btn_download_now);
        imgIcon.setOnClickListener(this);
        tvFileNameSize.setOnClickListener(this);
        tvCode.setOnClickListener(this);
        rtViewNum.setOnClickListener(this);
        rtLikeNum.setOnClickListener(this);
        rtDownNum.setOnClickListener(this);
        btnLookComment.setOnClickListener(this);
        btnDownloadNow.setOnClickListener(this);
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
        finish();
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
            case R.id.img_avatar:
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
}
