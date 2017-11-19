package com.sunny.youyun.wifidirect.model

import java.net.InetAddress

/**
 * Created by sunny on 17-11-16.
 */
class DeviceInfo constructor(var deviceAddress: InetAddress?, var mac: String,
                             var groupOwnerAddr: InetAddress?,
                             var isGroupOwner: Boolean = false, var deviceName: String = "",
                             var status: Status = Status.DISCONNECT)

enum class Status{
    DISCONNECT, CONNECT
}