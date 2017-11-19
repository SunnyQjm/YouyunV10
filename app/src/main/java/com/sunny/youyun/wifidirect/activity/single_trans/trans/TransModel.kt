package com.sunny.youyun.wifidirect.activity.single_trans.trans

import android.os.Handler

import com.orhanobut.logger.Logger
import com.sunny.youyun.App
import com.sunny.youyun.utils.UUIDUtil
import com.sunny.youyun.wifidirect.event.FileTransEvent
import com.sunny.youyun.wifidirect.model.TransLocalFile
import com.sunny.youyun.wifidirect.utils.TransRxBus
import com.sunny.youyun.wifidirect.socket.client.Client
import com.sunny.youyun.wifidirect.socket.server.Server
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by Sunny on 2017/8/11 0011.
 */

class TransModel(private val mPresenter: TransPresenter) : TransContract.Model {
    private val uuid = UUIDUtil.getUUID()
    private val mList = CopyOnWriteArrayList<TransLocalFile>()
    private val handler = Handler()

    override val data: List<TransLocalFile>
        get() = mList

    override fun begin() {
        TransRxBus.getInstance()
                .subscribe(uuid, { fileTransEvent ->
                    Logger.i(fileTransEvent.toString())
                    when (fileTransEvent.type ?: -1) {
                        FileTransEvent.Type.FINISH -> {
                            if (fileTransEvent.type == FileTransEvent.Type.UPLOAD) {
                                App.mList_SendRecord.add(mList[fileTransEvent.position])
                                return@subscribe
                            }
                            if (fileTransEvent.type == FileTransEvent.Type.DOWNLOAD) {
                                App.mList_ReceiveRecord.add(mList[fileTransEvent.position])
                            }
                            handler.post { mPresenter.update() }
                        }
                        FileTransEvent.Type.BEGIN -> handler.post { mPresenter.update() }
                        FileTransEvent.Type.UPLOAD, FileTransEvent.Type.DOWNLOAD -> {
                            println(fileTransEvent.already)
                            if (fileTransEvent.isDone || fileTransEvent.type == FileTransEvent.Type.BEGIN)
                                handler.post { mPresenter.update() }
                            if (mList.size <= fileTransEvent.position) {
                                Logger.i("mList.size() > fileTransEvent.getPosition()")
                                return@subscribe
                            }
                            handler.post { mPresenter.updateItem(mList.size - fileTransEvent.position - 1, mList[fileTransEvent.position]) }
                        }
                        FileTransEvent.Type.ERROR -> mPresenter.showError("传输失败")
                    }
                }) { throwable -> Logger.e(throwable, "面对面快传进度监听错误") }
        Server.INSTANCE
                .startSync()
        Server.INSTANCE
                .startSync()
    }

    override fun exit() {
        TransRxBus.getInstance().unSubscribe(uuid)
    }

    override fun send(ip: String, paths: Array<String>) {
        for (path in paths) {
            Client.INSTANCE
                    .sendFile(path, ip, mList)
        }
    }

}
