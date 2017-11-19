package com.sunny.youyun.wifidirect.socket.server

import com.sunny.youyun.wifidirect.model.TransLocalFile
import com.sunny.youyun.wifidirect.socket.Config
import com.sunny.youyun.wifidirect.utils.L
import com.sunny.youyun.wifidirect.utils.doInIOThread
import org.jetbrains.anko.doAsync
import java.io.IOException
import java.net.ServerSocket
import java.net.SocketException

/**
 * Created by sunny on 17-11-17.
 */
class Server constructor(private val listenPort: Int = Config.port,
                         private val serverSocketStrategy: ServerSocketStrategy
                         = ServerSocketImpl(),
                         var mList: MutableList<TransLocalFile> = mutableListOf())
    : ServerStrategy {

    override fun startSync() {
        if (isStop)
            doInIOThread {
                start()
            }
    }

    override fun stopSync() {
        doInIOThread {
            stop()
        }
    }

    private var isStop = true
    private var serverSocket = ServerSocket(listenPort)

    companion object {
        val INSTANCE = Server()
    }

    override fun start() {
        isStop = false
        if (serverSocket.isClosed)
            serverSocket = ServerSocket(listenPort)
        while (!isStop && serverSocket.isBound && !serverSocket.isClosed) {
            try {
                L.i("--------------------accepting---------------------")
                val socket = serverSocket.accept()
                L.i("--------------------accept success!-------------------")
                /**
                 * change to another thread to deal the request
                 * This thread still comeback to listen the request from client
                 */
                doAsync {
                    serverSocketStrategy.service(socket, mList)
                }
            } catch (e: SocketException) {
                //may be cause by call serverSocket.close()
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    override fun stop() {
        isStop = true
        try {
            serverSocket.close()
        } catch (e: IOException) {
            println("serverSocket close fail: ${e.message}")
        }
    }

}