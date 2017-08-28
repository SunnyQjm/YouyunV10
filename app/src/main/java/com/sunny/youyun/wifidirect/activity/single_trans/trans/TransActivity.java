package com.sunny.youyun.wifidirect.activity.single_trans.trans;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.FileManagerActivity;
import com.sunny.youyun.activity.file_manager.config.FileManagerRequest;
import com.sunny.youyun.base.BaseQuickAdapter;
import com.sunny.youyun.base.WifiDirectBaseActivity;
import com.sunny.youyun.utils.FileUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.youyun_dialog.tip.OnYouyunTipDialogClickListener;
import com.sunny.youyun.views.youyun_dialog.tip.YouyunTipDialog;
import com.sunny.youyun.wifidirect.activity.single_trans.adapter.FileRecordAdapter;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;
import com.sunny.youyun.wifidirect.model.TransLocalFile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Router(IndexRouter.TransActivity)
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("create trans");
        setContentView(R.layout.activity_trans);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.blue, null));
        } else {
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.blue));
        }
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        mPresenter.start();
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

        adapter = new FileRecordAdapter(R.layout.item_file_trans_record, mPresenter.getData());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
        adapter.setEmptyView(R.layout.recycler_empty_view);
    }

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
                            WifiDirectManager.getInstance().disConnect();
                            WifiDirectManager.getInstance().cancelConnect(null);
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
                mPresenter.send(results);
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
    protected void onDestroy() {
        System.out.println("----------Destroy---------");
        mPresenter.exit();
        super.onDestroy();
    }

    @Override
    public void update() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        position = mPresenter.getData().size() - 1 - position;
        if(mPresenter.getData().size() <= position)
            return;
        Intent intent = FileUtils.getOpenFileIntent(mPresenter.getData().get(position).getPath());
        if (intent != null)
            startActivity(intent);
        else
            Toast.makeText(this, "该文件无法打开", Toast.LENGTH_SHORT).show();
    }
}
