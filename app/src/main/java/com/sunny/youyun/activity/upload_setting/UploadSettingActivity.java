package com.sunny.youyun.activity.upload_setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.config.FileManagerRequest;
import com.sunny.youyun.activity.upload_setting.adapter.ExpandableItemAdapter;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.internet.upload.config.UploadConfig;
import com.sunny.youyun.model.YouyunAPI;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.ExpandableLineMenuItem;
import com.sunny.youyun.views.LineMenuItem;
import com.sunny.youyun.views.LineMenuSwitch;
import com.sunny.youyun.views.popupwindow.directory_select.DirectSelectPopupWindow;
import com.sunny.youyun.views.popupwindow.directory_select.DirectorySelectManager;
import com.sunny.youyun.views.youyun_dialog.data_picker.YouyunDatePickerDialog;
import com.sunny.youyun.views.youyun_dialog.edit.YouyunEditDialog;
import com.sunny.youyun.views.youyun_dialog.tip.OnYouyunTipDialogClickListener;
import com.sunny.youyun.views.youyun_dialog.tip.YouyunTipDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(value = IntentRouter.UploadSettingActivity)
public class UploadSettingActivity extends MVPBaseActivity<UploadSettingPresenter> implements UploadSettingContract.View, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.upload_setting_is_public)
    LineMenuSwitch uploadSettingIsPublic;
    @BindView(R.id.upload_setting_effect_date)
    ExpandableLineMenuItem uploadSettingEffectDate;
    @BindView(R.id.upload_setting_allow_down_count)
    ExpandableLineMenuItem uploadSettingAllowDownCount;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.upload_setting_effect_date_forever)
    LineMenuItem uploadSettingEffectDateForever;
    @BindView(R.id.upload_setting_effect_date_select)
    LineMenuItem uploadSettingEffectDateSelect;
    @BindView(R.id.upload_setting_allow_down_count_infinite)
    LineMenuItem uploadSettingAllowDownCountInfinite;
    @BindView(R.id.upload_setting_allow_down_count_edit)
    LineMenuItem uploadSettingAllowDownCountEdit;
    @BindView(R.id.upload_setting_score_zero)
    LineMenuItem uploadSettingScoreZero;
    @BindView(R.id.upload_setting_score_edit)
    LineMenuItem uploadSettingScoreEdit;
    @BindView(R.id.upload_setting_score)
    ExpandableLineMenuItem uploadSettingScore;
    @BindView(R.id.upload_setting_description_edit)
    EditText uploadSettingDescriptionEdit;
    @BindView(R.id.upload_setting_description)
    ExpandableLineMenuItem uploadSettingDescription;
    @BindView(R.id.upload_setting_description_img_sure)
    ImageView uploadSettingDescriptionImgSure;
    @BindView(R.id.upload_setting_select_directory)
    LineMenuItem uploadSettingSelectDirectory;
    private ExpandableItemAdapter adapter;
    private YouyunDatePickerDialog datePickerDialog = null;
    private YouyunTipDialog tipDialog = null;

    private static final int MAX = -1;

    private boolean isPublic = true;
    private int allowDownloadCount = MAX;
    private int downloadScore = 0;
    private long expireTime = MAX;
    private String parentId = null;
    private String pathName = "";

    private static final int REQUEST_PATH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        easyBar.setTitle(getString(R.string.upload_setting));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });

        uploadSettingIsPublic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //切换是否公开
            isPublic = isChecked;
        });

        //未登录的话，没有下面的可选项
        if(!YouyunAPI.isIsLogin()){
            uploadSettingSelectDirectory.setVisibility(View.GONE);
            uploadSettingScore.setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        String[] paths = intent.getStringArrayExtra(FileManagerRequest.KEY_PATH);
        pathName = intent.getStringExtra(FileManagerRequest.KEY_PATH_NAME);
        parentId = intent.getStringExtra(FileManagerRequest.KEY_PATH_ID);
        if(parentId != null && parentId.equals(""))
            parentId = null;
        adapter = new ExpandableItemAdapter(this, getMPresenter().getData(paths));
        adapter.setOnItemChildClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(this,
                DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        uploadSettingIsPublic.setChecked(isPublic);
        uploadSettingSelectDirectory.setValue(pathName != null && !pathName.equals("") ? pathName :
                getString(R.string.root_path));
    }

    @Override
    protected UploadSettingPresenter onCreatePresenter() {
        return new UploadSettingPresenter(this);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_sure, R.id.img_add,
            R.id.upload_setting_effect_date_forever, R.id.upload_setting_effect_date_select,
            R.id.upload_setting_allow_down_count_infinite, R.id.upload_setting_allow_down_count_edit,
            R.id.upload_setting_score_zero, R.id.upload_setting_score_edit,
            R.id.upload_setting_description_img_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                showCancelTipDialog();
                break;
            case R.id.img_add:
                RouterUtils.openForResult(this, IntentRouter.FileManagerActivity, REQUEST_PATH);
                break;
            case R.id.tv_sure:
                Intent intent = new Intent();
                intent.putExtra(UploadConfig.IS_PUBLIC, isPublic);
                intent.putExtra(UploadConfig.ALLOW_DOWNLOAD_COUNT, allowDownloadCount);
                intent.putExtra(UploadConfig.EFFECT_DATE, expireTime);
                intent.putExtra(UploadConfig.PATH, getMPresenter().getPaths());
                intent.putExtra(UploadConfig.DOWNLOAD_SCORE, downloadScore);
                intent.putExtra(UploadConfig.DESCRIPTION, uploadSettingDescriptionEdit.getText()
                        .toString());
                intent.putExtra(UploadConfig.PARENT_ID, parentId);
                setResult(0, intent);
                finish();
                break;
            case R.id.upload_setting_effect_date_forever:
                if (uploadSettingEffectDate != null) {
                    uploadSettingEffectDate.setValue(getString(R.string.forever));
                    uploadSettingEffectDate.close();
                }
                expireTime = MAX;
                break;
            case R.id.upload_setting_effect_date_select:
                showSelectDateDialog();
                break;
            case R.id.upload_setting_allow_down_count_infinite:
                if (uploadSettingAllowDownCount != null) {
                    uploadSettingAllowDownCount.setValue(getString(R.string.infinite));
                    uploadSettingAllowDownCount.close();
                }
                allowDownloadCount = -1;
                break;
            case R.id.upload_setting_allow_down_count_edit:
                showEditDownCountDialog();
                break;
            case R.id.upload_setting_score_zero:
                if (uploadSettingScore != null) {
                    uploadSettingScore.setValue(String.valueOf(0));
                    uploadSettingScore.close();
                }
                downloadScore = 0;
                break;
            case R.id.upload_setting_score_edit:
                showEditDownScoreDialog();
                break;
            case R.id.upload_setting_description_img_sure:
                uploadSettingDescription.close();
                break;
        }
    }

    private void showCancelTipDialog() {
        if (tipDialog == null)
            tipDialog = YouyunTipDialog.newInstance(R.drawable.icon_setting_cancel,
                    getString(R.string.cancelTip),
                    new OnYouyunTipDialogClickListener() {
                        @Override
                        public void onCancelClick() {

                        }

                        @Override
                        public void onSureClick() {
                            onBackPressed();
                        }
                    });
        tipDialog.show(getSupportFragmentManager(), "cancel_tip");
    }

    private void showEditDownCountDialog() {
        YouyunEditDialog.newInstance(getString(R.string.please_input_allow_download_count),
                allowDownloadCount == MAX ? "" : String.valueOf(allowDownloadCount), result -> {
                    allowDownloadCount = Integer.parseInt(result);
                    if (uploadSettingAllowDownCount != null) {
                        uploadSettingAllowDownCount.setValue(String.valueOf(result));
                        uploadSettingAllowDownCount.close();
                    }
                    if (uploadSettingAllowDownCountEdit != null) {
                        uploadSettingAllowDownCountEdit.setValue(String.valueOf(result));
                    }
                }).setInputType(InputType.TYPE_CLASS_NUMBER)
                .show(getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    private void showEditDownScoreDialog() {
        YouyunEditDialog.newInstance(getString(R.string.please_input_allow_download_count),
                downloadScore == 0 ? "" : String.valueOf(downloadScore), result -> {
                    downloadScore = Integer.parseInt(result);
                    if (uploadSettingScore != null) {
                        uploadSettingScore.setValue(String.valueOf(result));
                        uploadSettingScore.close();
                    }
                    if (uploadSettingScoreEdit != null) {
                        uploadSettingScoreEdit.setValue(String.valueOf(result));
                    }
                }).setInputType(InputType.TYPE_CLASS_NUMBER)
                .show(getSupportFragmentManager(), String.valueOf(this.getClass()));
    }

    public void showSelectDateDialog() {
        if (datePickerDialog == null)
            datePickerDialog = YouyunDatePickerDialog.newInstance(this, System.currentTimeMillis(),
                    new YouyunDatePickerDialog.OnYouyunDatePickerDialogClickListener() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSure(int year, int month, int day, long timestamps) {
                            expireTime = timestamps + 1000 * 60 * 60 * 24;
                            final String result = year + "-" + month + "-" + day;
                            if (uploadSettingEffectDateSelect != null)
                                uploadSettingEffectDateSelect.setValue(result);
                            if (uploadSettingEffectDate != null) {
                                uploadSettingEffectDate.setValue(result);
                                uploadSettingEffectDate.close();
                            }
                        }
                    });

        datePickerDialog.show(getFragmentManager(), "tag");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PATH:
                if (data == null)
                    return;
                String[] paths = data.getStringArrayExtra(FileManagerRequest.KEY_PATH);
                getMPresenter().addData(paths);
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter holder, View view, int position) {
        System.out.println(adapter.getData().size());
        switch (view.getId()) {
            case R.id.img_delete:
                getMPresenter().remove(position);
                adapter.removeItem(position);
                break;
        }
    }


    @Override
    public void updateUI() {
        if (adapter == null)
            return;
        adapter.notifyDataSetChanged();
        if (adapter.getData().size() != 0) {
            adapter.collapse(0);
            adapter.expand(0);
        }
    }

    /**
     * 选择文件夹
     */
    @OnClick(R.id.upload_setting_select_directory)
    public void onViewClicked() {
        DirectorySelectManager.getInstance(this)
                .setOnDismissListener(new DirectSelectPopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {

                    }

                    @Override
                    public void onResult(String pathName, String pathId) {
                        uploadSettingSelectDirectory.setValue(pathName);
                        parentId = pathId;
                    }
                })
                .show(uploadSettingAllowDownCount);
    }
}
