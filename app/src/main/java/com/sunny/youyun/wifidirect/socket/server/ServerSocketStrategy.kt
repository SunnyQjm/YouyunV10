package com.sunny.youyun.wifidirect.socket.server

import com.sunny.youyun.wifidirect.model.TransLocalFile
import java.net.Socket

/**
 * Created by sunny on 17-11-17.
 */
interface ServerSocketStrategy {
    fun service(socket: Socket, mReceiveList: MutableList<TransLocalFile>)
}