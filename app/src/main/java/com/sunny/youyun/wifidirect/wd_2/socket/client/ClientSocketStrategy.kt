package com.sunny.youyun.wifidirect.wd_2.socket.client

import java.net.InetAddress

/**
 * Created by sunny on 17-11-17.
 */
interface ClientSocketStrategy{
    /**
     * this method send a simple info to Server
     * to register this device to Group
     */
    fun register(address: InetAddress, port: Int)
}