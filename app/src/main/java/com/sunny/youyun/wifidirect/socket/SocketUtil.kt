package com.sunny.youyun.wifidirect.socket

import com.orhanobut.logger.Logger
import com.sunny.youyun.utils.MD5Util
import com.sunny.youyun.wifidirect.event.FileTransEvent
import com.sunny.youyun.wifidirect.info.CODE_TABLE
import com.sunny.youyun.wifidirect.model.SocketRequestBody
import com.sunny.youyun.wifidirect.model.TransLocalFile
import com.sunny.youyun.wifidirect.utils.GsonUtil
import com.sunny.youyun.wifidirect.utils.ProtocolStringBuilder
import com.sunny.youyun.wifidirect.utils.Tool
import com.sunny.youyun.wifidirect.extension.DelegatesExt
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

    fun flush() {
        out.flush()
    }

    fun readUTF(): String = input.readUTF()


    /**
     * 读文件
     *
     * @return
     */
    @Throws(IOException::class)
    fun readFile(transLocalFile: TransLocalFile, callBack: FileTransCallback?) {
        var fileOutputStream: FileOutputStream? = null
        val savePath = transLocalFile.path
        val file = File(savePath)
        //如果文件创建不成功
        file.createNewFile()
        if (!file.exists() || file.isDirectory)
            throw FileNotFoundException("保存路径无效")
        var isSuccess = false
        try {
            val buffer = ByteArray(1024 * 24)
            var alreadyTransBits: Long = 0
            var preTransBits: Long = 0
            val totalSize = transLocalFile.size
            var pre_process = 0
            var now_process = 0

            fileOutputStream = FileOutputStream(file)
            var length = 0
            callBack?.onBegin()
            val startTime = System.currentTimeMillis()
            var preTime = startTime
            while (true) {
                length = input.read(buffer)
                if (length == -1) {        //发送端的文件流关闭，
                    break
                }
                alreadyTransBits += length.toLong()
                fileOutputStream.write(buffer, 0, length)

                //获取当前文件的传输进度
                now_process = Math.ceil(alreadyTransBits * 1.0 / totalSize * 100).toInt()

                //作一层判断，当下载进度有明显变化时才更新进度
                if (now_process > pre_process) {
                    transLocalFile.progress = now_process      //更新下载进度

                    val span = System.currentTimeMillis() - preTime
                    if (span < 500)
                        continue
                    preTime = System.currentTimeMillis()
                    //间隔500ms计算速率
                    transLocalFile.rate = Tool.convertToRate(alreadyTransBits - preTransBits, span)
                    preTransBits = alreadyTransBits

                    pre_process = now_process
                    if (callBack != null) {
                        callBack!!.onProgress(FileTransEvent.Builder()
                                .already(alreadyTransBits)
                                .type(FileTransEvent.Type.DOWNLOAD)
                                .total(totalSize)
                                .done(alreadyTransBits == totalSize)
                                .build()
                        )
                    }

                }
            }
            fileOutputStream.flush()

            transLocalFile.rate = Tool.convertToRate(totalSize, System.currentTimeMillis() - startTime)
            transLocalFile.createTime = System.currentTimeMillis()
            transLocalFile.path = savePath
            transLocalFile.isDone = true
            //传输完毕后再发一次
            callBack?.onEnd()
            //            TransRxBus.getInstance().post(new FileTransEvent.Builder()
            //                    .already(alreadyTransBits)
            //                    .total(totalSize)
            //                    .done(true)
            //                    .position(position)
            //                    .type(FileTransEvent.Type.DOWNLOAD)
            //                    .build());
            isSuccess = true
        } catch (e: IOException) {
            e.printStackTrace()
            isSuccess = false
            callBack?.onError(e)
            Logger.e(e, "readFile failed！")
        } finally {
            //关闭文件输出流即可，Socket不需要手动关闭
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            if (isSuccess) {
                val md5 = MD5Util.getFileMD5(FileInputStream(file))
                println("md5: " + md5!!)
                transLocalFile.md5 = md5
                transLocalFile.saveOrUpdate("md5 = ?", md5)
            }
        }
    }

    /**
     * 写文件
     *
     * @return
     */
    @Throws(IOException::class)
    fun writeFile(transLocalFile: TransLocalFile, callback: FileTransCallback?) {
        val file = File(transLocalFile.path)
        if (!file.exists() || file.isDirectory) {
            if (callback != null)
                callback!!.onError(FileNotFoundException("文件不存在"))
            throw FileNotFoundException("文件不存在")
        }

        var fileInputStream: FileInputStream? = null

        val buffer = ByteArray(1024 * 24)
        var length: Int
        var pre_process = 0
        var now_process: Int
        val totalSize = file.length()
        val startTime = System.currentTimeMillis()
        var preTime = startTime
        var alreadyTransBits = 0
        var preTransBits = 0
        val position: Int
        var isSuccess = false
        try {
            fileInputStream = FileInputStream(file)
            val body = SocketRequestBody(
                    CODE_TABLE.REQUEST_SINGLE_FILE, "传输单个文件", transLocalFile)

            var str = GsonUtil.bean2Json(body)
            str = ProtocolStringBuilder.getInstance()
                    .contract(body.code)
                    .contract(str)
                    .toString()
            //发送文件基本信息
            writeUTF(str)
            //开始传输
            callback?.onBegin()
            while (true) { //如果还没发完
                length = fileInputStream.read(buffer)
                if(length < 0)
                    break
                out.write(buffer, 0, length)
                out.flush()
                alreadyTransBits += length
                now_process = Math.ceil(alreadyTransBits * 1.0 / totalSize * 100).toInt()

                //作一层判断，当下载进度有明显变化时才更新进度
                if (now_process > pre_process) {
                    transLocalFile.progress = now_process      //更新下载进度
                    pre_process = now_process

                    val span = System.currentTimeMillis() - preTime
                    if (span < 500) {
                        continue
                    }
                    preTime = System.currentTimeMillis()
                    transLocalFile.rate = Tool.convertToRate((alreadyTransBits - preTransBits).toLong(), span)
                    preTransBits = alreadyTransBits

                    if (callback != null)
                        callback!!.onProgress(FileTransEvent.Builder()
                                .already(alreadyTransBits.toLong())
                                .total(totalSize)
                                .done(alreadyTransBits.toLong() == totalSize)
                                .type(FileTransEvent.Type.UPLOAD)
                                .build())
                }
            }

            transLocalFile.rate = Tool.convertToRate(totalSize, System.currentTimeMillis() - startTime)
            transLocalFile.createTime = System.currentTimeMillis()
            transLocalFile.isDone = true
            callback?.onEnd()
            isSuccess = true
        } catch (e: IOException) {
            isSuccess = false
            Logger.e(e, "发送文件失败")
            callback?.onError(e)
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            if (isSuccess) {
                val md5 = MD5Util.getFileMD5(FileInputStream(file))
                println("md5: " + md5!!)
                transLocalFile.md5 = md5
                transLocalFile.saveOrUpdate("md5 = ?", md5)
            }
        }
    }

    fun close() {
        out.close()
        input.close()
        socket.close()
    }

    interface FileTransCallback {
        fun onBegin()

        fun onProgress(fileTransEvent: FileTransEvent)

        fun onEnd()

        fun onError(e: Exception)
    }
}