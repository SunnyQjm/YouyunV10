package com.sunny.youyun.wifidirect.wd_2.socket.client

import com.sunny.youyun.wifidirect.wd_2.manager.WifiDirectManager
import com.sunny.youyun.wifidirect.wd_2.socket.ProtocolCode
import com.sunny.youyun.wifidirect.wd_2.socket.SocketUtil
import com.sunny.youyun.wifidirect.wd_2.utils.GsonUtil
import com.sunny.youyun.wifidirect.wd_2.utils.L
import com.sunny.youyun.wifidirect.wd_2.utils.doInMainThread
import java.net.InetAddress
import java.net.Socket

/**
 *
 * Created by sunny on 17-11-17.
 */
class ClientSocketImpl : ClientSocketStrategy {
    override fun register(address: InetAddress, port: Int) {
        var su: SocketUtil? = null
        try {
            L.i("begin register connect")
            val s = Socket(address.hostAddress, port)
            su = SocketUtil(s)
            su.writeInt(ProtocolCode.CODE_REGISTER)
            su.writeUTF(
                    GsonUtil.bean2Json(WifiDirectManager.INSTANCE
                            .myDeviceInfo ?: ""))
            su.flush()
            L.i("receive: " + su.readInt())
            doInMainThread {
                WifiDirectManager.INSTANCE
                        .mListener?.onRegisterSuccess()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                su?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}