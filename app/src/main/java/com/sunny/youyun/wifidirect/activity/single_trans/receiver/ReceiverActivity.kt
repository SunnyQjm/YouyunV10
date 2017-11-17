package com.sunny.youyun.wifidirect.activity.single_trans.receiver

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.github.mzule.activityrouter.annotation.Router
import com.sunny.youyun.IntentRouter
import com.sunny.youyun.R
import com.sunny.youyun.activity.file_manager.FileManagerActivity
import com.sunny.youyun.activity.scan.ScanActivity
import com.sunny.youyun.base.adapter.BaseQuickAdapter
import com.sunny.youyun.base.activity.WifiDirectBaseActivity
import com.sunny.youyun.utils.RouterUtils
import com.sunny.youyun.views.EasyBar
import com.sunny.youyun.wifidirect.activity.single_trans.receiver.config.ReceiverFragmentConfig
import com.sunny.youyun.wifidirect.utils.NetworkUtils

import butterknife.ButterKnife
import com.sunny.youyun.base.RecyclerViewDividerItem
import com.sunny.youyun.base.adapter.BaseViewHolder
import com.sunny.youyun.wifidirect.activity.single_trans.adapter.PeersAdapter
import com.sunny.youyun.wifidirect.wd_2.manager.WifiDirectManager
import com.sunny.youyun.wifidirect.wd_2.model.DeviceInfo
import kotlinx.android.synthetic.main.activity_receiver.*
import kotlinx.android.synthetic.main.bar_item.*

@Router(IntentRouter.ReceiverActivity)
class ReceiverActivity : WifiDirectBaseActivity<ReceiverPresenter>()
        , ReceiverContract.View, View.OnClickListener {

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnScanQrCode -> {
                val intent = Intent(this, ScanActivity::class.java)
                startActivityForResult(intent, ReceiverFragmentConfig.REQUEST_SCAN)
            }
            R.id.btnAddFile -> {
                intent = Intent(this, FileManagerActivity::class.java)
                startActivityForResult(intent, ReceiverFragmentConfig.REQUEST_FILE)
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        //开启Wifi功能
        NetworkUtils.setWifiEnable(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)
        changeStatusBarColor(R.color.blue)
        ButterKnife.bind(this)
        initView()
    }


    private fun initView() {
        WifiDirectManager.INSTANCE
                .discoverPeers()
                .mListener = object : WifiDirectManager.OnWifiDirectListener {
            override fun onWifiP2pDeviceListChange(list: MutableList<DeviceInfo>) {
                if (recyclerView.adapter == null) {
                    val adapter = PeersAdapter(mutableListOf())
                    recyclerView.adapter = adapter
                    adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { a, v, position ->
                        WifiDirectManager.INSTANCE
                                .connect((a.getItem(position) as DeviceInfo).mac)
                    }
                }
                val b = recyclerView.adapter as BaseQuickAdapter<DeviceInfo, *>
                b.data.clear()
                b.data.addAll(list)
                recyclerView.adapter.notifyDataSetChanged()
                lv.visibility = View.INVISIBLE
            }

            override fun onRegisterSuccess() {
                connectSuccess(WifiDirectManager.INSTANCE
                        .myDeviceInfo
                        ?.groupOwnerAddr
                        ?.hostAddress ?: "")
            }
        }
        easyBar.setTitle(getString(R.string.i_want_to_receive))
        easyBar.setOnEasyBarClickListener(object : EasyBar.OnEasyBarClickListener {
            override fun onLeftIconClick(view: View) {
                onBackPressed()
            }

            override fun onRightIconClick(view: View) {

            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(RecyclerViewDividerItem(this,
                RecyclerViewDividerItem.VERTICAL))
        btnScanQrCode.setOnClickListener(this)
        btnAddFile.setOnClickListener(this)
        lv.loadingText = getString(R.string.searching)
    }

    override fun onCreatePresenter(): ReceiverPresenter = ReceiverPresenter(this)

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ReceiverFragmentConfig.REQUEST_FILE -> {
                if (data == null)
                    return
                val paths = data.getStringArrayExtra(ReceiverFragmentConfig.REQUEST_FILE_RESULT_KEY)

                for (path in paths) {
                }
            }
            ReceiverFragmentConfig.REQUEST_SCAN -> {
                if (data == null)
                    return
                val result = data.getStringExtra(ReceiverFragmentConfig.REQUEST_SCAN_RESULT_KEY)
                showLoading()
                WifiDirectManager.INSTANCE
                        .connect(result)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        WifiDirectManager.INSTANCE
                .removeGroup()
    }

    fun connectSuccess(ip: String) {
        dismissDialog()
        RouterUtils.open(this, IntentRouter.TransActivity, ip)
        finish()
    }
}
