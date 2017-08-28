package com.sunny.youyun.wifidirect.activity.single_trans.receiver;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.App;
import com.sunny.youyun.IndexRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.FileManagerActivity;
import com.sunny.youyun.activity.scan.ScanActivity;
import com.sunny.youyun.base.BaseQuickAdapter;
import com.sunny.youyun.base.WifiDirectBaseActivity;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.RichText;
import com.sunny.youyun.views.loading_view.LoadingView;
import com.sunny.youyun.wifidirect.activity.single_trans.adapter.PeersAdapter;
import com.sunny.youyun.wifidirect.activity.single_trans.receiver.config.ReceiverFragmentConfig;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;
import com.sunny.youyun.wifidirect.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@Router(IndexRouter.ReceiverActivity)
public class ReceiverActivity extends WifiDirectBaseActivity<ReceiverPresenter> implements ReceiverContract.View, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.btn_scan_qr_code)
    RichText btnScanQrCode;
    @BindView(R.id.btn_add_file)
    RichText btnAddFile;
    Unbinder unbinder;
    @BindView(R.id.shape_loading_view)
    LoadingView shapeLoadingView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private PeersAdapter adapter;
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
        super.onResume();
        //开启Wifi功能
        NetworkUtils.setWifiEnable(this, true);
        if (shapeLoadingView != null && mList.size() == 0)
            shapeLoadingView.setVisibility(View.VISIBLE);
        WifiDirectManager.WifiDirectListeners.getInstance().bindPeerListListener(peerListListener);
        WifiDirectManager.getInstance().discover();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
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
    protected ReceiverPresenter onCreatePresenter() {
        return new ReceiverPresenter(this);
    }


    @OnClick({R.id.btn_scan_qr_code, R.id.btn_add_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scan_qr_code:
                Intent intent = new Intent(this, ScanActivity.class);
                startActivityForResult(intent, ReceiverFragmentConfig.REQUEST_SCAN);
                break;
            case R.id.btn_add_file:
                intent = new Intent(this, FileManagerActivity.class);
                startActivityForResult(intent, ReceiverFragmentConfig.REQUEST_FILE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ReceiverFragmentConfig.REQUEST_FILE:
                if (data == null)
                    return;
                String[] paths = data.getStringArrayExtra(ReceiverFragmentConfig.REQUEST_FILE_RESULT_KEY);

                for (String path : paths) {
                }
                break;
            case ReceiverFragmentConfig.REQUEST_SCAN:
                if (data == null)
                    return;
                String result = data.getStringExtra(ReceiverFragmentConfig.REQUEST_SCAN_RESULT_KEY);
                showLoading();
                mPresenter.connect(result, null);
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        showLoading();
        mPresenter.connect(mList.get(position).deviceAddress, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        WifiDirectManager.getInstance().disConnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.exit();
    }

    @Override
    public void connectSuccess() {
        dismissDialog();
        RouterUtils.open(this, IndexRouter.TransActivity);
        App.startAnim(this);
        finish();
    }
}
