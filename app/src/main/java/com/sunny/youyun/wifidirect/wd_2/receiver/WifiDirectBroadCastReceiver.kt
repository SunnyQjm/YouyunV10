package com.sunny.youyun.wifidirect.wd_2.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.*
import android.os.Build
import com.google.gson.Gson
import com.sunny.youyun.wifidirect.wd_2.manager.WifiDirectManager
import com.sunny.youyun.wifidirect.wd_2.utils.L

/**
 * Created by sunny on 17-11-16.
 */
class WifiDirectBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("onReceive: ${intent?.action}")
        if (context == null || intent == null)
            return
        when (intent.action) {
        //this action indicate whether wifi p2p is enable
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                //get the state of current device
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,
                        WifiP2pManager.WIFI_P2P_STATE_DISABLED)
                println("state: $state")
            }

        //indicate this device detail change
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                val device: WifiP2pDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)
                WifiDirectManager.INSTANCE
                        .updateDeviceInfo(device)
                L.json(Gson().toJson(device))
            }

        //invoke when the list of peers find, register, lost
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                //api > 18 have this extra info,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    val wifiP2pList: WifiP2pDeviceList
                            = intent.getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST)
                    WifiDirectManager.INSTANCE
                            .updateWifiP2pDeviceList(wifiP2pList)
                } else { //if the sdk version lower than 18
                    //get WifiP2pDeviceList by call WifiP2pManager.requestPeers to get
                    WifiDirectManager.INSTANCE
                            .requestPeers()
                }


            }
        //This action received when the connection setup or dis-setup
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiP2pManager.EXTRA_NETWORK_INFO)
                val wifiP2pInfo = intent.getParcelableExtra<WifiP2pInfo>(WifiP2pManager.EXTRA_WIFI_P2P_INFO)
                WifiDirectManager.INSTANCE
                        .updateDeviceInfo(wifiP2pInfo)
                        .updateNetWorkInfo(networkInfo)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    val wifiP2pGroupInfo
                            = intent.getParcelableExtra<WifiP2pGroup>(WifiP2pManager.EXTRA_WIFI_P2P_GROUP)
                    WifiDirectManager.INSTANCE
                            .updateWifiP2pGroup(wifiP2pGroup = wifiP2pGroupInfo)
                } else {
                    WifiDirectManager.INSTANCE
                            .requestGroup()
                }
                // 如果当前还没有建立群组，则不做任何操作
                if (wifiP2pInfo.groupFormed)
                    return

            }
        }
    }

}