package com.sunny.youyun.wifidirect.wd_2.socket.server

import java.net.Socket

/**
 * Created by sunny on 17-11-17.
 */
interface ServerSocketStrategy {
    fun service(socket: Socket)
}