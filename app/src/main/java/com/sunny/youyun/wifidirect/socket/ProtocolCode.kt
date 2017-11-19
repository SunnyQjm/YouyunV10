package com.sunny.youyun.wifidirect.socket

/**
 * Created by sunny on 17-11-17.
 */
object ProtocolCode{
    @JvmStatic val CODE_REGISTER = 0
    @JvmStatic val REQUEST_ADD_FORWARD_ITEM = 1          //通知服务器添加表项
    @JvmStatic val REQUEST_DELETE_FORWARD_ITEM = 2
    @JvmStatic val REQUEST_SIMPLE_TEXT = 3        //收到简单字符串
    @JvmStatic val REQUEST_SINGLE_FILE = 4        //传送单个文件
}