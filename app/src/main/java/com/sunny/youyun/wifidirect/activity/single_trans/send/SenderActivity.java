package com.sunny.youyun.wifidirect.activity.single_trans.send;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.file_manager.FileManagerActivity;
import com.sunny.youyun.activity.file_manager.config.FileManagerRequest;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.base.activity.WifiDirectBaseActivity;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.EasyBar;
import com.sunny.youyun.views.RichText;
import com.sunny.youyun.views.loading_view.LoadingView;
import com.sunny.youyun.views.youyun_dialog.qr.YouyunQRDialog;
import com.sunny.youyun.wifidirect.activity.single_trans.adapter.PeersAdapter;
import com.sunny.youyun.wifidirect.activity.single_trans.trans.TransActivity;
import com.sunny.youyun.wifidirect.config.EventConfig;
import com.sunny.youyun.wifidirect.event.BaseEvent;
import com.sunny.youyun.wifidirect.manager.DeviceInfoManager;
import com.sunny.youyun.wifidirect.manager.SingleTransManager;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@Router(IntentRouter.SenderActivity)
public class SenderActivity extends WifiDirectBaseActivity<SenderPresenter>
        implements SenderContract.View, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.btn_qr_code)
    RichText btnQrCode;
    @BindView(R.id.btn_add_file)
    RichText btnAddFile;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.shape_loading_view)
    LoadingView shapeLoadingView;
    @BindView(R.id.easyBar)
    EasyBar easyBar;

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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault()
                .register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault()
                .unregister(this);
    }

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
        changeStatusBarColor(R.color.blue);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        getMPresenter().start();
        easyBar.setTitle(getString(R.string.i_want_to_send));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });
        adapter = new PeersAdapter(mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);

        shapeLoadingView.setLoadingText(getString(R.string.searching));
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
        getMPresenter().connect(mList.get(position), null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMPresenter().exit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventListen(BaseEvent<String> baseEvent) {
        switch (baseEvent.getCode()) {
            case EventConfig.FORWARD_SIGNAL_ADD:
                break;
            case EventConfig.FORWARD_SIGNAL_DELETE:
                break;
            case EventConfig.IP_CHANGE:
                if (baseEvent.getData() != null) {
                    SingleTransManager.getInstance().getTargetInfo().setIp((String) baseEvent.getData());
                    System.out.println(SingleTransManager.getInstance().getTargetInfo());
                    Logger.i("targetInfo: " + SingleTransManager.getInstance().getTargetInfo());
                    Logger.i("myInfo: " + SingleTransManager.getInstance().getMyInfo());
                    Logger.i("groupInfo: " + DeviceInfoManager.getInstance().getGroupOwnerIp());
                    connectSuccess();
                }
                break;
        }
    }

    @Override
    public void connectSuccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            RouterUtils.openWithAnimation(this, new Intent(this, TransActivity.class));
        } else {
            RouterUtils.open(this, IntentRouter.TransActivity);
        }
        finish();
    }
}
