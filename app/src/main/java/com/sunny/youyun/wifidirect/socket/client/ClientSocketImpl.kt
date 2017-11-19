package com.sunny.youyun.wifidirect.socket.client

import com.sunny.youyun.wifidirect.event.FileTransEvent
import com.sunny.youyun.wifidirect.model.SocketRequestBody
import com.sunny.youyun.wifidirect.model.TransLocalFile
import com.sunny.youyun.wifidirect.utils.ProtocolStringBuilder
import com.sunny.youyun.wifidirect.utils.TransRxBus
import com.sunny.youyun.wifidirect.manager.WifiDirectManager
import com.sunny.youyun.wifidirect.socket.ProtocolCode
import com.sunny.youyun.wifidirect.socket.SocketUtil
import com.sunny.youyun.wifidirect.utils.GsonUtil
import com.sunny.youyun.wifidirect.utils.L
import com.sunny.youyun.wifidirect.utils.doInMainThread
import java.io.File
import java.io.IOException
import java.net.InetAddress
import java.net.Socket

/**
 *
 * Created by sunny on 17-11-17.
 */
class ClientSocketImpl : ClientSocketStrategy {

    @Throws(IOException::class)
    private fun <T> send(socket: Socket, body: SocketRequestBody<T>, code: Int) {
        val su = SocketUtil(socket)

        var str = GsonUtil.bean2Json(body)
        str = ProtocolStringBuilder.getInstance()
                .contract(code)
                .contract(str)
                .toString()

        su.writeUTF(str)      //将信息发给服务端
        val ret = su.readUTF()
        println("服务器返回来的消息为: " + ret)
        su.close()
    }

    override fun sendText(socket: Socket, info: String) {
        val body = SocketRequestBody(ProtocolCode.REQUEST_SIMPLE_TEXT, "send text",
                info)
        send(socket, body, ProtocolCode.REQUEST_SIMPLE_TEXT)
    }


    override fun sendSingleFile(socket: Socket, file: File, mList: MutableList<TransLocalFile>): Boolean {
        var socketUtils: SocketUtil? = null
        var result = false
        try {
            socketUtils = SocketUtil(socket)
            val transLocalFile = TransLocalFile.Builder()
                    .name(file.name)
                    .path(file.path)
                    .size(file.length())
                    .fileTAG(TransLocalFile.TAG_SEND)
                    .createTime(System.currentTimeMillis())
                    .build()
            var position = 0
            synchronized(SocketUtil::class.java) {
                position = mList.size
                mList.add(transLocalFile)
            }

            socketUtils.writeFile(transLocalFile, object : SocketUtil.FileTransCallback {
                override fun onBegin() {
                    TransRxBus.getInstance().post(FileTransEvent.Builder()
                            .done(false)
                            .position(position)
                            .type(FileTransEvent.Type.BEGIN)
                            .build())
                }

                override fun onProgress(fileTransEvent: FileTransEvent) {
                    TransRxBus.getInstance().post(FileTransEvent.Builder()
                            .already(fileTransEvent.already)
                            .total(fileTransEvent.total)
                            .done(fileTransEvent.isDone)
                            .position(position)
                            .type(FileTransEvent.Type.UPLOAD)
                            .build())
                }

                override fun onEnd() {
                    TransRxBus.getInstance().post(FileTransEvent.Builder()
                            .done(true)
                            .position(position)
                            .type(FileTransEvent.Type.FINISH)
                            .build())
                }

                override fun onError(e: Exception) {
                    TransRxBus.getInstance().post(FileTransEvent.Builder()
                            .exception(e)
                            .position(position)
                            .type(FileTransEvent.Type.ERROR)
                            .build())
                }
            })
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (socketUtils != null)
                try {
                    socketUtils.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

        }
        return result
    }

    override fun register(address: InetAddress, port: Int) {
        var su: SocketUtil? = null
        try {
            L.i("begin register connect")
            val s = Socket(address.hostAddress, port)
            su = SocketUtil(s)
            su.writeInt(ProtocolCode.CODE_REGISTER)
            su.writeUTF(
                    GsonUtil.bean2Json(WifiDirectManager.INSTANCE
                            .myDeviceInfo ?: ""))
            su.flush()
            L.i("receive: " + su.readInt())
            doInMainThread {
                WifiDirectManager.INSTANCE
                        .mListener?.onRegisterSuccess()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                su?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}