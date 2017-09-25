package com.sunny.youyun.activity.file_detail_online;

import android.Manifest;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.App;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_detail_online.adapter.CommentAdapter;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.internet.download.FileDownloader;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.ShareContent;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.utils.GlideUtils;
import com.sunny.youyun.utils.InputMethodUtil;
import com.sunny.youyun.utils.MyClickText;
import com.sunny.youyun.utils.RxPermissionUtil;
import com.sunny.youyun.utils.Tool;
import com.sunny.youyun.utils.WindowUtil;
import com.sunny.youyun.utils.bus.ObjectPool;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.RichText;
import com.sunny.youyun.views.easy_refresh.EasyRefreshLayout;
import com.sunny.youyun.views.youyun_dialog.share.ShareDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(value = IntentRouter.FileDetailOnlineActivity + "/:uuid", stringParams = "uuid")
public class FileDetailOnlineActivity extends MVPBaseActivity<FileDetailOnlinePresenter> implements FileDetailOnlineContract.View, EasyRefreshLayout.OnRefreshListener, EasyRefreshLayout.OnLoadListener, View.OnClickListener {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.refreshLayout)
    EasyRefreshLayout refreshLayout;
    @BindView(R.id.et_comment_content)
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

    private View header;
    private CommentAdapter adapter;
    @BindView(R.id.recyclerView_comment)
    RecyclerView recyclerViewComment;
//    @BindView(R.id.refreshLayout)
//    EasyRefreshLayout refreshLayout;

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

        String uuid = getIntent().getStringExtra("uuid");
        internetFile = ObjectPool.getInstance().get(uuid, InternetFile.empty());
        fillData();

        mPresenter.getFileInfo(internetFile.getIdentifyCode());

        mPresenter.getComments(internetFile.getId(), true);
    }

    /**
     * 填充数据
     */
    private void fillData() {
        if (internetFile.getUser() == null)
            tvDescription.setText(internetFile.getDescription());
        else {
            SpannableString spannableString = buildDescription();
//            User user = internetFile.getUser();
//            GlideUtils.load(this, imgAvatar, user.getAvatar());
            tvDescription.setText(spannableString);
//            tvUserSShare.setText(String.format("%s%s", user.getUsername(), getString(R.string.xxs_share)));
        }
        if(!internetFile.isCanStar()){
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
                    Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
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
            case R.id.rt_view_count:
                break;
            case R.id.rt_like_count:
//                rtLikeNum.setDrawableRes(R.drawable.icon_zan_selected);
                mPresenter.star(internetFile.getId());
                break;
            case R.id.rt_down_count:
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

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        //TODO 发表评论
        if (YouyunAPI.isIsLogin()) {
            mPresenter.addComment(internetFile.getId(), etCommentContent.getText().toString());
            etCommentContent.setText("");
            InputMethodUtil.hide(this);
        } else {
            showTip(getString(R.string.login_first));
        }
    }

    @Override
    public void commentSuccess() {
        mPresenter.getComments(internetFile.getId(), false);
    }

    @Override
    public void getCommentsSuccess(boolean isFirst) {
        if(adapter == null)
            return;
        if (!isFirst) {
            adapter.notifyDataSetChanged();
            //滚动到最后一条，因为有头布局，所以position要加1
            recyclerViewComment.scrollToPosition(mPresenter.getCommentList().size());
        }else {
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
        //点赞成功后重新获取文件信息
        mPresenter.getFileInfo(internetFile.getIdentifyCode());
    }
}
