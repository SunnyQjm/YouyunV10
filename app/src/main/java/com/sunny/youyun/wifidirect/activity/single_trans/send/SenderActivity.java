package com.sunny.youyun.wifidirect.activity.single_trans.send;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.App;
import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.FileManagerActivity;
import com.sunny.youyun.activity.file_manager.config.FileManagerRequest;
import com.sunny.youyun.base.BaseQuickAdapter;
import com.sunny.youyun.base.WifiDirectBaseActivity;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.RichText;
import com.sunny.youyun.views.loading_view.LoadingView;
import com.sunny.youyun.views.youyun_dialog.qr.YouyunQRDialog;
import com.sunny.youyun.wifidirect.activity.single_trans.adapter.PeersAdapter;
import com.sunny.youyun.wifidirect.manager.DeviceInfoManager;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@Router(IndexRouter.SenderActivity)
public class SenderActivity extends WifiDirectBaseActivity<SenderPresenter> implements SenderContract.View, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.btn_qr_code)
    RichText btnQrCode;
    @BindView(R.id.btn_add_file)
    RichText btnAddFile;
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.shape_loading_view)
    LoadingView shapeLoadingView;

    private PeersAdapter adapter;
    private YouyunQRDialog dialog;
    private final List<WifiP2pDevice> mList = new ArrayList<>();

    private final WifiP2pManager.PeerListListener peerListListener = peers -> {
        mList.clear();
        mList.addAll(peers.getDeviceList());
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (shapeLoadingView != null) {
            if (mList.size() > 0) {
                shapeLoadingView.setVisibility(View.INVISIBLE);
            } else {
                shapeLoadingView.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onResume() {
        System.out.println("onResume");
        WifiDirectManager.getInstance().createGroup(new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Logger.i("创建群组成功");
            }

            @Override
            public void onFailure(int reason) {
                Logger.e("创建群组失败：" + reason);
            }
        });

        if (shapeLoadingView != null)
            shapeLoadingView.setVisibility(View.VISIBLE);
        WifiDirectManager.WifiDirectListeners.getInstance().bindPeerListListener(peerListListener);
        WifiDirectManager.getInstance().discover();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sender);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mPresenter.start();
        adapter = new PeersAdapter(mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);

        shapeLoadingView.setLoadingText("正在搜索...");
    }

    @Override
    protected SenderPresenter onCreatePresenter() {
        return new SenderPresenter(this);
    }

    @OnClick({R.id.btn_qr_code, R.id.btn_add_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_qr_code:
                if (dialog == null) {
                    dialog = YouyunQRDialog.newInstance(this, DeviceInfoManager.getInstance().getGroupOwnerMacAddr());
//                    dialog = new YouyunQRDialog(this, DeviceInfoManager.getInstance().getGroupOwnerMacAddr());
                }
                dialog.show(getSupportFragmentManager(), String.valueOf(this.getClass()));
                break;
            case R.id.btn_add_file:
                Intent intent = new Intent(this, FileManagerActivity.class);
                startActivityForResult(intent, FileManagerRequest.REQUEST_PATH);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        WifiDirectManager.getInstance().disConnect();
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FileManagerRequest.REQUEST_PATH:
                if (data == null)
                    return;
                String[] paths = data.getStringArrayExtra(FileManagerRequest.KEY_PATH);

                for (String path : paths) {
                }
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mPresenter.connect(mList.get(position), null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.exit();
    }

    @Override
    public void connectSuccess() {
        RouterUtils.open(this, IndexRouter.TransActivity);
        App.startAnim(this);
        finish();
    }
}
