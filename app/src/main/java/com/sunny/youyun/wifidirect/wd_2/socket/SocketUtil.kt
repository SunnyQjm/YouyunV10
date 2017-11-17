package com.sunny.youyun.wifidirect.wd_2.socket

import com.sunny.youyun.wifidirect.wd_2.extension.DelegatesExt
import java.io.*
import java.net.Socket

/**
 * Created by sunny on 17-11-17.
 */
class SocketUtil(val socket: Socket) {
    private var out by DelegatesExt.notNullSingleValue<DataOutputStream>()
    private var input by DelegatesExt.notNullSingleValue<DataInputStream>()

    init {
        out = DataOutputStream(socket.getOutputStream())
        input = DataInputStream(socket.getInputStream())
    }

    fun writeInt(i: Int) {
        out.writeInt(i)
    }

    fun readInt(): Int = input.readInt()

    fun writeUTF(info: String) {
        out.writeUTF(info)
    }

    fun flush(){
        out.flush()
    }

    fun readUTF(): String = input.readUTF()


    fun close() {
        out.close()
        input.close()
        socket.close()
    }
}