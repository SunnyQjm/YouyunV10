package com.sunny.youyun.wifidirect.wd_2.socket.client

/**
 * Created by sunny on 17-11-17.
 */
interface ClientStrategy{
    /**
     * this method send a simple info to server
     * to register this device to Group
     */
    fun register()

    fun registerSync()

}