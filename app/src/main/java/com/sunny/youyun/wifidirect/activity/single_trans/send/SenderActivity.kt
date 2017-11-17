package com.sunny.youyun.wifidirect.activity.single_trans.send

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.github.mzule.activityrouter.annotation.Router
import com.sunny.youyun.IntentRouter
import com.sunny.youyun.R
import com.sunny.youyun.activity.file_manager.FileManagerActivity
import com.sunny.youyun.activity.file_manager.config.FileManagerRequest
import com.sunny.youyun.base.adapter.BaseQuickAdapter
import com.sunny.youyun.base.activity.WifiDirectBaseActivity
import com.sunny.youyun.utils.RouterUtils
import com.sunny.youyun.views.EasyBar
import com.sunny.youyun.wifidirect.activity.single_trans.adapter.PeersAdapter


import butterknife.ButterKnife
import com.sunny.youyun.wifidirect.wd_2.manager.WifiDirectManager
import com.sunny.youyun.wifidirect.wd_2.model.DeviceInfo
import com.sunny.youyun.wifidirect.wd_2.utils.GsonUtil
import kotlinx.android.synthetic.main.bar_item.*
import kotlinx.android.synthetic.main.fragment_sender.*
import org.jetbrains.anko.toast

@Router(IntentRouter.SenderActivity)
class SenderActivity : WifiDirectBaseActivity<SenderPresenter>(), SenderContract.View, View.OnClickListener {
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnQrCode -> {
                showQrDialog(com.sunny.youyun.wifidirect.wd_2.manager.WifiDirectManager
                        .Companion.INSTANCE
                        .myDeviceInfo
                        ?.mac)
            }
            R.id.btnAddFile -> {
                val intent = Intent(this, FileManagerActivity::class.java)
                startActivityForResult(intent, FileManagerRequest.REQUEST_PATH)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sender)
        changeStatusBarColor(R.color.blue)
        ButterKnife.bind(this)
        initView()
    }

    private fun initView() {

        WifiDirectManager.INSTANCE
                .createGroup()
                .discoverPeers()
                .mListener = object : WifiDirectManager.OnWifiDirectListener {
            override fun onWifiP2pDeviceListChange(list: MutableList<DeviceInfo>) {
                if (recyclerView.adapter == null) {
                    val adapter = PeersAdapter(WifiDirectManager.INSTANCE
                            .wifiDeviceList)
                    recyclerView.adapter = adapter
                    adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { a, v, position ->
                        WifiDirectManager.INSTANCE
                                .connect((a.getItem(position) as DeviceInfo).mac)
                    }
                }
                recyclerView.adapter.notifyDataSetChanged()
                lv.visibility = View.INVISIBLE
            }

            override fun onNewDeviceRegister(deviceInfo: DeviceInfo) {
                toast(GsonUtil.bean2Json(deviceInfo))
                connectSuccess(deviceInfo.deviceAddress
                        ?.hostAddress ?: "")
            }
        }
        easyBar.setTitle(getString(R.string.i_want_to_send))
        easyBar.setOnEasyBarClickListener(object : EasyBar.OnEasyBarClickListener {
            override fun onLeftIconClick(view: View) {
                onBackPressed()
            }

            override fun onRightIconClick(view: View) {

            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        lv.loadingText = getString(R.string.searching)

        btnQrCode.setOnClickListener(this)
        btnAddFile.setOnClickListener(this)
    }

    override fun onCreatePresenter(): SenderPresenter = SenderPresenter(this)


    override fun onBackPressed() {
        com.sunny.youyun.wifidirect.wd_2.manager.WifiDirectManager
                .Companion.INSTANCE
                .removeGroup()
        super.onBackPressed()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FileManagerRequest.REQUEST_PATH -> {
                if (data == null)
                    return
                val paths = data.getStringArrayExtra(FileManagerRequest.KEY_PATH)

                for (path in paths) {
                }
            }
        }
    }
    fun connectSuccess(ip: String) {
        RouterUtils.open(this, IntentRouter.TransActivity, ip)
        finish()
    }
}
