package com.sunny.youyun.wifidirect.activity.single_trans.trans;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mzule.activityrouter.annotation.Router;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.FileManagerActivity;
import com.sunny.youyun.activity.file_manager.config.FileManagerRequest;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.activity.WifiDirectBaseActivity;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.utils.RxPermissionUtil;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.youyun_dialog.tip.OnYouyunTipDialogClickListener;
import com.sunny.youyun.views.youyun_dialog.tip.YouyunTipDialog;
import com.sunny.youyun.wifidirect.activity.single_trans.adapter.FileRecordAdapter;
import com.sunny.youyun.wifidirect.manager.SingleTransManager;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;
import com.sunny.youyun.wifidirect.model.TransLocalFile;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IntentRouter.TransActivity + "/:ip")
public class TransActivity extends WifiDirectBaseActivity<TransPresenter>
        implements TransContract.View, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.easyBar)
    EasyBar easyBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_addFile)
    TextView btnAddFile;

    private FileRecordAdapter adapter;
    private YouyunTipDialog dialog = null;

    private String ip = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("create trans");
        setContentView(R.layout.activity_trans);
        changeStatusBarColor(R.color.blue);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            // TODO: Consider calling
            RxPermissionUtil.getInstance(this)
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(aBoolean -> {
                        if(aBoolean){
                            initView();
                        } else {
                            finish();
                        }
                    });
            return;
        }
        initView();
    }


    @RequiresPermission(allOf = Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void initView() {
        ip = getIntent().getStringExtra("ip");
        //初始化
        getMPresenter().start();
        easyBar.setTitle("正在传输");
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });

        adapter = new FileRecordAdapter(R.layout.item_file_trans_record, getMPresenter().getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
        adapter.setEmptyView(R.layout.recycler_empty_view);
    }

    @NonNull
    @Override
    protected TransPresenter onCreatePresenter() {
        return new TransPresenter(this);
    }


    @Override
    public void onBackPressed() {
        if (dialog == null) {
            dialog = YouyunTipDialog.newInstance(R.drawable.icon_transfer_back,
                    "退出将终止传输，是否退出？",
                    new OnYouyunTipDialogClickListener() {
                        @Override
                        public void onCancelClick() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onSureClick() {
                            TransActivity.super.onBackPressed();
                        }
                    });
        }
        dialog.show(getSupportFragmentManager(), String.valueOf(TransActivity.class));
    }

    @OnClick(R.id.btn_addFile)
    public void onViewClicked() {
        Intent intent = new Intent(this, FileManagerActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (data == null)
                    return;
                String[] results = data.getStringArrayExtra(FileManagerRequest.KEY_PATH);
                getMPresenter().send(ip, results);
                break;
        }
    }

    @Override
    public void updateItem(int position, TransLocalFile transLocalFile) {
        if (adapter != null && adapter.getData().size() > position) {
            adapter.notifyItemChanged(position, transLocalFile);
        }
    }


    @Override
    public void finish() {
        WifiDirectManager.getInstance().disConnect();
        WifiDirectManager.getInstance().cancelConnect(null);
        try {
            WifiDirectManager.getInstance().stopServer();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e("关闭服务监听失败", e);
        }
        SingleTransManager.getInstance().clearInfo();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        System.out.println("----------Destroy---------");
        getMPresenter().exit();
        super.onDestroy();
    }

    @Override
    public void update() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        position = getMPresenter().getData().size() - 1 - position;
        if (getMPresenter().getData().size() <= position)
            return;
        Intent intent = FileUtils.getOpenFileIntent(getMPresenter().getData().get(position).getPath());
        if (intent != null)
            startActivity(intent);
        else
            Toast.makeText(this, "该文件无法打开", Toast.LENGTH_SHORT).show();
    }
}
