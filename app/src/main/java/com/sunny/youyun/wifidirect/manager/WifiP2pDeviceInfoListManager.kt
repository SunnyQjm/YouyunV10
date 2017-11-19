package com.sunny.youyun.wifidirect.manager

import android.net.wifi.p2p.WifiP2pDevice
import com.sunny.youyun.wifidirect.model.DeviceInfo

/**6
 * Created by sunny on 17-11-17.
 */
class WifiP2pDeviceInfoListManager {
    companion object {
        val INSTANCE = WifiP2pDeviceInfoListManager()
    }

    val mList = mutableListOf<DeviceInfo>()
    private val map = HashMap<String, DeviceInfo>()


    fun updateList(list: Collection<WifiP2pDevice>) {
        val map = mutableMapOf<String, WifiP2pDevice>()
        list.forEach {
            map.put(it.deviceAddress, it)
        }
        //First, delete the device disconnect
        mList.forEach {
            if (map[it.mac] != null) {
                map.remove(it.mac)
                mList.remove(it)
            }
        }
        list.forEach {
                    add(DeviceInfo(null, it.deviceAddress, null,
                            it.isGroupOwner, it.deviceName))
                }
    }

    /**
     * Call this method to add a Device to list
     */
    fun add(deviceInfo: DeviceInfo) {
        val dev: DeviceInfo? = map[deviceInfo.mac]
        if (dev == null) {
            map.put(deviceInfo.mac, deviceInfo)
            mList.add(deviceInfo)
        }
    }

    /**
     * Call this method to register device
     * 1. at the begin, we can only know the MAC address about each device in list
     * 2. when a device try to connect current device (assume current device is group owner)
     * 3. the client device will request a simple connection, then the group owner can get that device's IP address
     * 4. then call this method to update that device's info in list
     */
    fun register(deviceInfo: DeviceInfo) {
        val dev: DeviceInfo? = map[deviceInfo.mac]
        if (dev != null) {
            dev.deviceAddress = deviceInfo.deviceAddress
            dev.deviceName = deviceInfo.deviceName
            dev.status = deviceInfo.status
        }
    }

    fun clear() {
        mList.clear()
        map.clear()
    }

}