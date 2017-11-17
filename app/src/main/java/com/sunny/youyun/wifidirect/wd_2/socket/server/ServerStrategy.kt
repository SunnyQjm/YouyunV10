package com.sunny.youyun.wifidirect.wd_2.socket.server

/**
 * Created by sunny on 17-11-17.
 */
interface ServerStrategy{
    /**
     * this method invoke begin to listen the connect from client
     */
    fun start()

    fun startSync()

    /**
     * this method invoke stop to listen
     */
    fun stop()

    fun stopSync()
}