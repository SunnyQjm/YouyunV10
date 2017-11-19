package com.sunny.youyun.wifidirect.socket.server

import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import com.sunny.youyun.utils.FileUtils
import com.sunny.youyun.wifidirect.event.FileTransEvent
import com.sunny.youyun.wifidirect.info.CODE_TABLE
import com.sunny.youyun.wifidirect.model.SocketRequestBody
import com.sunny.youyun.wifidirect.model.TransLocalFile
import com.sunny.youyun.wifidirect.utils.TransRxBus
import com.sunny.youyun.wifidirect.manager.WifiDirectManager
import com.sunny.youyun.wifidirect.model.DeviceInfo
import com.sunny.youyun.wifidirect.model.Status
import com.sunny.youyun.wifidirect.socket.ProtocolCode
import com.sunny.youyun.wifidirect.socket.SocketUtil
import com.sunny.youyun.wifidirect.utils.GsonUtil
import com.sunny.youyun.wifidirect.utils.L
import com.sunny.youyun.wifidirect.utils.doInMainThread
import java.io.IOException
import java.net.Socket


/**
 * Created by sunny on 17-11-17.
 */
class ServerSocketImpl : ServerSocketStrategy {

    override fun service(socket: Socket, mReceiveList: MutableList<TransLocalFile>) {
        println("service")
        var su: SocketUtil? = null
        try {
            su = SocketUtil(socket)
            val code: Int = su.readInt()
            L.i("code: " + code)
            when (code) {
            //Client register
                ProtocolCode.CODE_REGISTER -> {
                    val json = su.readUTF()
                    L.i("json: " + json)
                    val deviceInfo = GsonUtil.json2Bean(json, DeviceInfo::class.java)
                    deviceInfo.deviceAddress = socket.inetAddress
                    deviceInfo.status = Status.CONNECT
                    WifiDirectManager.INSTANCE
                            .update(deviceInfo)
                    doInMainThread {
                        WifiDirectManager.INSTANCE
                                .mListener?.onNewDeviceRegister(deviceInfo)
                    }
                }

                CODE_TABLE.REQUEST_SIMPLE_TEXT -> {
                    //将对方的IP写回，以便对方获取IP
                    su.writeUTF(socket.inetAddress.hostAddress)
                }
                CODE_TABLE.REQUEST_SINGLE_FILE -> {
                    val body = GsonUtil.json2Bean<SocketRequestBody<TransLocalFile>>(
                            su.readUTF(), object : TypeToken<SocketRequestBody<TransLocalFile>>() {}.type)
                    val transLocalFile = body.obj
                    transLocalFile.fileTAG = TransLocalFile.TAG_RECEIVE
                    val path = FileUtils.getWifiDirectPath() + transLocalFile.name
                    transLocalFile.path = path
                    println("传输的文件名：" + transLocalFile.name)
                    println("文件的大小为：" + transLocalFile.size)
                    println("文件接收路径：" + path)
                    println()
                    var position: Int = 0
                    synchronized(SocketUtil::class.java) {
                        position = mReceiveList.size
                        mReceiveList.add(transLocalFile)
                    }
                    receiveFile(su, position, transLocalFile)
                    println("文件读取完毕")
                }
            }
            su.writeInt(-1)
            su.flush()
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            try {
                su?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun receiveFile(socketUtils: SocketUtil?, position: Int, localFile: TransLocalFile) {
        try {
            socketUtils?.readFile(localFile, object : SocketUtil.FileTransCallback {
                override fun onBegin() {
                    TransRxBus.getInstance().post(FileTransEvent.Builder()
                            .already(0)
                            .total(0)
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
                            .type(FileTransEvent.Type.DOWNLOAD)
                            .build())
                }

                override fun onEnd() {
                    TransRxBus.getInstance().post(FileTransEvent.Builder()
                            .already(0)
                            .total(0)
                            .done(false)
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
        } catch (e: IOException) {
            e.printStackTrace()
            Logger.e("写入文件IO错误", e)
        }

    }

}