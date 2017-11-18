package com.sunny.youyun.wifidirect.wd_2.socket.client

import com.sunny.youyun.wifidirect.config.SocketConfig
import com.sunny.youyun.wifidirect.model.TransLocalFile
import com.sunny.youyun.wifidirect.wd_2.socket.Config
import com.sunny.youyun.wifidirect.wd_2.utils.doInIOThread
import java.io.File
import java.net.InetAddress
import java.net.Socket

/**
 * Created by sunny on 17-11-17.
 */
class Client(private val strategy: ClientSocketStrategy = ClientSocketImpl()) : ClientStrategy {
    override fun sendFile(path: String, ip: String, mList: MutableList<TransLocalFile>) {
        try {
            val socket = Socket(ip, SocketConfig.FileListenPort)
            val f = File(path)
            if (!f.exists())
                return
            strategy.sendSingleFile(socket, f, mList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        val INSTANCE = Client()
    }

    override fun registerSync(address: InetAddress, port: Int) {
        doInIOThread {
            register(address, port)
        }
    }

    override fun register(address: InetAddress, port: Int) {
        strategy.register(address, port)
    }


}