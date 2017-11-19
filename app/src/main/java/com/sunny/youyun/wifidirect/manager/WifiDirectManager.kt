package com.sunny.youyun.wifidirect.manager

import android.content.Context
import android.net.NetworkInfo
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.*
import android.os.Looper
import com.google.gson.Gson
import com.sunny.youyun.wifidirect.model.DeviceInfo
import com.sunny.youyun.wifidirect.socket.client.Client
import com.sunny.youyun.wifidirect.socket.server.Server
import com.sunny.youyun.wifidirect.utils.L
import com.sunny.youyun.wifidirect.utils.doInMainThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.properties.Delegates

/**
 *
 * Created by sunny on 17-11-16.
 */
class WifiDirectManager {

    //Provider by api, use to create p2p connection with other device which support wifi-direct
    private var manager: WifiP2pManager by Delegates.notNull()
    private var channel: WifiP2pManager.Channel by Delegates.notNull()


    var mListener: OnWifiDirectListener? = null
    /**
     * Current Device's Info
     */
    var myDeviceInfo: DeviceInfo? = null

    /**
     * Network Info
     */
    private lateinit var networkInfo: NetworkInfo

    /**
     * the under tow properties use for searching and select one device to connect
     */
    lateinit var wifiP2pDeviceList: WifiP2pDeviceList
    val wifiDeviceList = mutableListOf<DeviceInfo>()
    val groupList = WifiP2pDeviceInfoListManager.INSTANCE

    private lateinit var wifiP2pGroup: WifiP2pGroup

    companion object {
        val INSTANCE = WifiDirectManager()
    }

    fun init(context: Context): WifiDirectManager {
        manager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(context, Looper.getMainLooper()) {

        }
        myDeviceInfo = DeviceInfo(null, "", null)
        return this
    }


    /////////////////////////////////////////////////////////////////////////////////
    //////////// Provider Some interface to record info about WifiDirect
    /////////////////////////////////////////////////////////////////////////////////
    /**
     * Update Device info
     */
    fun updateDeviceInfo(wifiP2pInfo: WifiP2pInfo): WifiDirectManager {
        myDeviceInfo?.groupOwnerAddr = wifiP2pInfo.groupOwnerAddress
        myDeviceInfo?.isGroupOwner = wifiP2pInfo.isGroupOwner
        if (myDeviceInfo?.isGroupOwner == true)
            myDeviceInfo?.deviceAddress = wifiP2pInfo.groupOwnerAddress
        println("register Device Info: ")
        println(Gson().toJson(wifiP2pInfo))
        //if group formed, and this device is not the group owner
        //then send a simple socket to group owner for register itself
        if (wifiP2pInfo.groupFormed && !wifiP2pInfo.isGroupOwner) {
            L.i("try to register")
            Client.INSTANCE
                    .register(wifiP2pInfo.groupOwnerAddress)
        }

        //if group formed, and this device is the group owner
        //then open a socket to listen the request from client
        if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
            Server.INSTANCE
                    .startSync()
        }
        if(myDeviceInfo != null)
            doInMainThread {
                mListener?.onDeviceInfoChange(myDeviceInfo!!)
            }
        return this
    }

    fun updateDeviceInfo(wifiP2pDevice: WifiP2pDevice): WifiDirectManager {
        myDeviceInfo?.mac = wifiP2pDevice.deviceAddress
        myDeviceInfo?.deviceName = wifiP2pDevice.deviceName
        return this
    }

    fun clearDeviceInfo() {
        myDeviceInfo?.isGroupOwner = false
        myDeviceInfo?.groupOwnerAddr = null
        myDeviceInfo?.deviceAddress = null
        myDeviceInfo?.mac = ""
        myDeviceInfo?.deviceName = ""
    }

    /**
     * Update NetWork info
     */
    fun updateNetWorkInfo(networkInfo: NetworkInfo): WifiDirectManager {
        this.networkInfo = networkInfo
        doInMainThread {
            mListener?.onNetWorkInfoChange(networkInfo)
        }
        return this
    }

    /**
     * register the available peer list
     */
    fun updateWifiP2pDeviceList(list: WifiP2pDeviceList): WifiDirectManager {
        doAsync {
            wifiP2pDeviceList = list
            wifiDeviceList.clear()
            list.deviceList
                    .forEach {
                        wifiDeviceList.add(DeviceInfo(null, it.deviceAddress, null,
                                it.isGroupOwner, it.deviceName))
                    }
            uiThread {
                mListener?.onWifiP2pDeviceListChange(wifiDeviceList)
            }
        }
        return this
    }

    fun update(deviceInfo: DeviceInfo): WifiDirectManager {
        groupList.register(deviceInfo)
        doInMainThread {
            mListener?.onWifiP2pDeviceListChange(groupList.mList)
        }
        return this
    }


    /**
     * register WifiP2pGroup info
     * this information normal use in communication
     */
    fun updateWifiP2pGroup(wifiP2pGroup: WifiP2pGroup): WifiDirectManager {
        this.wifiP2pGroup = wifiP2pGroup
        groupList.updateList(wifiP2pGroup.clientList)
        doInMainThread {
            mListener?.onWifiP2pGroupChange(wifiP2pGroup)
        }
        return this
    }


    ////////////////////////////////////////////////////////////////////
    //////////// Provide some interface to easy operate the origin API provider by Google
    ////////////////////////////////////////////////////////////////////
    /**
     * request the peer list available
     */
    fun requestPeers(): WifiDirectManager {
        manager.requestPeers(channel) { peers ->
            updateWifiP2pDeviceList(peers)
        }
        return this
    }

    /**
     * request the group info
     */
    fun requestGroup(): WifiDirectManager {
        manager.requestGroupInfo(channel) { group ->
            updateWifiP2pGroup(group)
        }
        return this
    }

    /**
     * discover available peer list
     */
    fun discoverPeers(): WifiDirectManager {
        manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                println("discover success")
                doInMainThread {
                    mListener?.discoverPeers(true)
                }
            }

            override fun onFailure(reason: Int) {
                println("discover fail: $reason")
                doInMainThread {
                    mListener?.discoverPeers(false, reason)
                }
            }

        })
        return this
    }


    /**
     * create a group
     */
    fun createGroup(): WifiDirectManager {
        manager.createGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                println("create group success")
                doInMainThread {
                    mListener?.createGroup(true)
                }
            }

            override fun onFailure(reason: Int) {
                println("create group fail: $reason")
                doInMainThread {
                    mListener?.createGroup(false, reason)
                }
            }
        })
        return this
    }

    /**
     * remove a group
     */
    fun removeGroup(): WifiDirectManager {
        manager.removeGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                println("remove group success")
                // when remove a group, clear the device info
                INSTANCE
                        .clearDeviceInfo()
                Server.INSTANCE
                        .stop()
                doInMainThread {
                    mListener?.removeGroup(true)
                }
            }

            override fun onFailure(reason: Int) {
                println("remove group success: $reason")
                doInMainThread {
                    mListener?.removeGroup(false, reason)
                }
            }

        })
        return this
    }

    /**
     * connect by MAC address(hardware address)
     */
    fun connect(deviceAddress: String) {
        val config = WifiP2pConfig()
        config.deviceAddress = deviceAddress
        config.wps.setup = WpsInfo.PBC
        manager.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                println("connect operator success")
                doInMainThread {
                    mListener?.connect(true)
                }
            }

            override fun onFailure(reason: Int) {
                println("connect operator fail: " + reason)
                doInMainThread {
                    mListener?.connect(false, reason)
                }
            }
        })
    }

    /**
     * invoke this method to connect a p2p device
     */
    fun connect(device: WifiP2pDevice): WifiDirectManager {
        connect(device.deviceAddress)
        return this
    }

    fun cancelConnect(): WifiDirectManager {
        manager.cancelConnect(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                println("cancel connect success")
                mListener?.onCancelConnect(true)
            }

            override fun onFailure(reason: Int) {
                println("cancel connect fail: $reason")
                mListener?.onCancelConnect(false, reason)
            }
        })
        return this
    }

    /**
     * Provide this method to re_name current device
     * Because this method is hide by API
     * so I try to do this by java reflection
     */
    fun setDeviceName(name: String): WifiDirectManager {
        //利用反射试图修改设备的名字
        val c = WifiP2pManager::class.java
        val m = c.getMethod("setDeviceName", WifiP2pManager.Channel::class.java, String::class.java,
                WifiP2pManager.ActionListener::class.java)
        m.isAccessible = true
        m.invoke(manager, channel, name, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                L.i("modify device info success")
            }

            override fun onFailure(reason: Int) {
                L.i("modify device info fail: $reason")
            }

        })
        return this
    }


    /////////////////////////////////////////////////////////////////////////
    /////////// Define callback
    /////////////////////////////////////////////////////////////////////////

    interface OnWifiDirectListener {
        fun createGroup(isSuccess: Boolean, reason: Int = -1) {

        }

        fun removeGroup(isSuccess: Boolean, reason: Int = -1) {

        }

        fun discoverPeers(isSuccess: Boolean, reason: Int = -1) {

        }

        fun onDeviceInfoChange(deviceInfo: DeviceInfo) {

        }

        fun onNetWorkInfoChange(networkInfo: NetworkInfo) {

        }

        fun onWifiP2pDeviceListChange(list: MutableList<DeviceInfo>) {

        }

        fun onWifiP2pGroupChange(wifiP2pGroup: WifiP2pGroup) {

        }

        fun connect(isSuccess: Boolean, reason: Int = -1) {

        }

        fun onCancelConnect(isSuccess: Boolean, reason: Int = -1) {

        }

        /**
         * when the client connect to Group owner and register success
         * this method will be invoked
         */
        fun onRegisterSuccess() {

        }

        fun onNewDeviceRegister(deviceInfo: DeviceInfo) {

        }
    }

}
