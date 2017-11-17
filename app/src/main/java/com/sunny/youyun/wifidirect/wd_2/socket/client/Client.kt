package com.sunny.youyun.wifidirect.wd_2.socket.client

import com.sunny.youyun.wifidirect.wd_2.socket.Config
import com.sunny.youyun.wifidirect.wd_2.utils.doInIOThread
import java.net.InetAddress

/**
 * Created by sunny on 17-11-17.
 */
class Client(private val address: InetAddress, private val port: Int = Config.port,
             private val strategy: ClientSocketStrategy = ClientSocketImpl()): ClientStrategy {

    override fun registerSync() {
        doInIOThread {
            register()
        }
    }

    override fun register() {
        strategy.register(address, port)
    }


}