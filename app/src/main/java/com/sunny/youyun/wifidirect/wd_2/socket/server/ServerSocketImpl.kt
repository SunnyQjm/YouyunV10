package com.sunny.youyun.wifidirect.wd_2.socket.server

import com.sunny.youyun.wifidirect.wd_2.manager.WifiDirectManager
import com.sunny.youyun.wifidirect.wd_2.model.DeviceInfo
import com.sunny.youyun.wifidirect.wd_2.model.Status
import com.sunny.youyun.wifidirect.wd_2.socket.ProtocolCode
import com.sunny.youyun.wifidirect.wd_2.socket.SocketUtil
import com.sunny.youyun.wifidirect.wd_2.utils.GsonUtil
import com.sunny.youyun.wifidirect.wd_2.utils.L
import com.sunny.youyun.wifidirect.wd_2.utils.doInMainThread
import java.net.Socket


/**
 * Created by sunny on 17-11-17.
 */
class ServerSocketImpl: ServerSocketStrategy {

    override fun service(socket: Socket) {
        println("service")
        var su: SocketUtil? = null
        try {
            su = SocketUtil(socket)
            val code: Int = su.readInt()
            L.i("code: " + code)
            when(code){
            //Client register
                ProtocolCode.CODE_REGISTER ->{
                    val json = su.readUTF()
                    L.i("json: " + json)
                    val deviceInfo = GsonUtil.json2Bean(json, DeviceInfo::class.java)
                    deviceInfo.deviceAddress = socket.inetAddress
                    deviceInfo.status = Status.CONNECT
                    WifiDirectManager.INSTANCE
                            .update(deviceInfo)
                    doInMainThread {
                        WifiDirectManager.INSTANCE
                                .mListener?.onNewDeviceRegister(deviceInfo)
                    }
                }
            }
            su.writeInt(-1)
            su.flush()
        } catch (e: Throwable){
            e.printStackTrace()
        } finally {
            try {
                su?.close()
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

    }

}