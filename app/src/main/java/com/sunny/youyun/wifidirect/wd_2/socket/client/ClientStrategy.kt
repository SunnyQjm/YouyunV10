package com.sunny.youyun.wifidirect.wd_2.socket.client

import com.sunny.youyun.wifidirect.model.TransLocalFile
import com.sunny.youyun.wifidirect.wd_2.socket.Config
import java.net.InetAddress

/**
 * Created by sunny on 17-11-17.
 */
interface ClientStrategy{
    /**
     * this method send a simple info to server
     * to register this device to Group
     */
    fun register(address: InetAddress, port: Int = Config.port)

    fun registerSync(address: InetAddress, port: Int = Config.port)

    fun sendFile(path: String, ip: String, mList: MutableList<TransLocalFile>)
}