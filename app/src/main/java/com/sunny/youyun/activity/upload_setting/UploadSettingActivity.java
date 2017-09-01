package com.sunny.youyun.activity.upload_setting;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.config.FileManagerRequest;
import com.sunny.youyun.activity.upload_setting.adapter.ExpandableItemAdapter;
import com.sunny.youyun.base.activity.MVPBaseActivity;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.LineMenuItem;
import com.sunny.youyun.views.LineMenuSwitch;
import com.sunny.youyun.views.YouyunDatePickerDialog;
import com.sunny.youyun.views.YouyunEditDialog;
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
    LineMenuItem uploadSettingEffectDate;
    @BindView(R.id.upload_setting_allow_down_count)
    LineMenuItem uploadSettingAllowDownCount;
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
    private ExpandableItemAdapter adapter;
    private YouyunDatePickerDialog datePickerDialog = null;
    private YouyunEditDialog editDialog = null;
    private YouyunTipDialog tipDialog = null;

    private String[] paths = null;
    public static final String IS_PUBLIC = "is public";
    public static final String ALLOW_DOWNLOAD_COUNT = "allow download count";
    public static final String EFFECT_DATE = "effect date";
    public static final String PATH = "path";
    private static final int MAX = -1;

    private boolean isPublic = true;
    private int allowDownloadCount = MAX;
    private long expireTime = MAX;

    private boolean isExpireTimeOptionVisible = false;
    private boolean isAllowDownCountOptionVisible = false;
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

        });


        Intent intent = getIntent();
        paths = intent.getStringArrayExtra("paths");
        adapter = new ExpandableItemAdapter(this, mPresenter.getData(paths));
        adapter.setOnItemChildClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        uploadSettingIsPublic.setChecked(isPublic);
    }

    @Override
    protected UploadSettingPresenter onCreatePresenter() {
        return new UploadSettingPresenter(this);
    }

    @OnClick({R.id.upload_setting_effect_date, R.id.upload_setting_allow_down_count, R.id.tv_cancel, R.id.tv_sure, R.id.img_add,
            R.id.upload_setting_effect_date_forever, R.id.upload_setting_effect_date_select,
            R.id.upload_setting_allow_down_count_infinite, R.id.upload_setting_allow_down_count_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.upload_setting_effect_date:
                changeExpireDataOption();
                break;
            case R.id.upload_setting_allow_down_count:
                changeAllowDownCountOption();
                break;
            case R.id.tv_cancel:
                showCancelTipDialog();
                break;
            case R.id.img_add:
                RouterUtils.openForResult(this, IntentRouter.FileManagerActivity, REQUEST_PATH);
                break;
            case R.id.tv_sure:
                Intent intent = new Intent();
                intent.putExtra(IS_PUBLIC, isPublic);
                intent.putExtra(ALLOW_DOWNLOAD_COUNT, allowDownloadCount);
                intent.putExtra(EFFECT_DATE, expireTime);
                intent.putExtra(PATH, paths);
                setResult(0, intent);
                finish();
                break;
            case R.id.upload_setting_effect_date_forever:
                if (uploadSettingEffectDate != null)
                    uploadSettingEffectDate.setValue(getString(R.string.forever));
                expireTime = MAX;
                changeExpireDataOption();
                break;
            case R.id.upload_setting_effect_date_select:
                showSelectDateDialog();
                break;
            case R.id.upload_setting_allow_down_count_infinite:
                if (uploadSettingAllowDownCount != null)
                    uploadSettingAllowDownCount.setValue(getString(R.string.infinite));
                allowDownloadCount = -1;
                changeAllowDownCountOption();
                break;
            case R.id.upload_setting_allow_down_count_edit:
                showEditDownCountDialog();
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
        if (editDialog == null) {
            editDialog = YouyunEditDialog.newInstance("请输入可下载次数",
                    result -> {
                        allowDownloadCount = Integer.parseInt(result);
                        if (uploadSettingAllowDownCount != null)
                            uploadSettingAllowDownCount.setValue(String.valueOf(result));
                        if (uploadSettingAllowDownCountEdit != null)
                            uploadSettingAllowDownCountEdit.setValue(String.valueOf(result));
                        changeAllowDownCountOption();
                    });
        }
        editDialog.show(getSupportFragmentManager(), String.valueOf(this.getClass()));
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
                            if (uploadSettingEffectDate != null)
                                uploadSettingEffectDate.setValue(result);
                            changeExpireDataOption();
                        }
                    });

        datePickerDialog.show(getFragmentManager(), "tag");
    }

    private void changeAllowDownCountOption() {
        if (!isAllowDownCountOptionVisible) {
            ObjectAnimator.ofFloat(uploadSettingAllowDownCount.getRight_icon(), "rotation", 0f, 90f)
                    .setDuration(300)
                    .start();
            uploadSettingAllowDownCountInfinite.setVisibility(View.VISIBLE);
            uploadSettingAllowDownCountEdit.setVisibility(View.VISIBLE);
            isAllowDownCountOptionVisible = true;
        } else {
            ObjectAnimator.ofFloat(uploadSettingAllowDownCount.getRight_icon(), "rotation", 90f, 0f)
                    .setDuration(300)
                    .start();
            uploadSettingAllowDownCountInfinite.setVisibility(View.GONE);
            uploadSettingAllowDownCountEdit.setVisibility(View.GONE);
            isAllowDownCountOptionVisible = false;
        }
    }

    private void changeExpireDataOption() {
        if (!isExpireTimeOptionVisible) {
            ObjectAnimator.ofFloat(uploadSettingEffectDate.getRight_icon(), "rotation", 0f, 90f)
                    .setDuration(300)
                    .start();
            uploadSettingEffectDateForever.setVisibility(View.VISIBLE);
            uploadSettingEffectDateSelect.setVisibility(View.VISIBLE);
            isExpireTimeOptionVisible = true;
        } else {
            ObjectAnimator.ofFloat(uploadSettingEffectDate.getRight_icon(), "rotation", 90f, 0f)
                    .setDuration(300)
                    .start();
            uploadSettingEffectDateForever.setVisibility(View.GONE);
            uploadSettingEffectDateSelect.setVisibility(View.GONE);
            isExpireTimeOptionVisible = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_PATH:
                if (data == null)
                    return;
                String[] paths = data.getStringArrayExtra(FileManagerRequest.KEY_PATH);
                mPresenter.addData(paths);
                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter holder, View view, int position) {
        System.out.println(adapter.getData().size());
        switch (view.getId()) {
            case R.id.img_delete:
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


}
