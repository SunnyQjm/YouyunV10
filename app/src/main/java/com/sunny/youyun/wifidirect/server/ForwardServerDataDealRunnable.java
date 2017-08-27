package com.sunny.youyun.wifidirect.server;

import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.sunny.youyun.wifidirect.info.CODE_TABLE;
import com.sunny.youyun.wifidirect.manager.ForwardTableManager;
import com.sunny.youyun.wifidirect.model.DeviceInfo;
import com.sunny.youyun.wifidirect.model.SocketRequestBody;
import com.sunny.youyun.wifidirect.utils.CheckSumUtil;
import com.sunny.youyun.wifidirect.utils.GsonUtil;
import com.sunny.youyun.wifidirect.utils.MyThreadPool;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.LinkedBlockingQueue;

public class ForwardServerDataDealRunnable implements Runnable {
    private LinkedBlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();
    private volatile boolean isStop;

    void add(byte[] data){
        if(data == null || data.length == 0)
            return;
        queue.add(data);
    }

    private boolean isStop() {
        return isStop;
    }

    public void stop(){
        isStop = true;
    }

    @Override
    public void run() {
        byte[] dataWithCheckSum = new byte[0];
        int length = dataWithCheckSum.length;

        //如果线程池关闭，则结束循环，优雅的退出
        while (!MyThreadPool.getInstance().isShutdownJudge() && !isStop()){
            try {
                dataWithCheckSum = queue.take();
                length = dataWithCheckSum.length;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] chuckSum = CheckSumUtil.getCRC32Value(dataWithCheckSum, 0, length - 4);
            boolean isRight = true;

            for (int i = length - 4; i < length; i++) {
                if(chuckSum[i - length + 8] != dataWithCheckSum[i])
                    isRight = false;
            }
            if(isRight){
                try {
                    String info = new String(dataWithCheckSum, 0, length - 4, "utf-8");
                    SocketRequestBody<DeviceInfo> body = GsonUtil.getInstance().fromJson(info,
                            new TypeToken<SocketRequestBody<DeviceInfo>>(){}.getType());
                    switch (body.getCode()){
                        //添加表项
                        case CODE_TABLE.REQUEST_ADD_FORWARD_ITEM:
                            boolean result = ForwardTableManager.getInstance()
                                    .addOrUpdate(body.getObj());

                            if(result) {
                                Logger.i("添加或更新表项成功");
                            }
                            else
                                Logger.i("添加或更新表项失败");
                            break;

                        //删除 表项
                        case CODE_TABLE.REQUEST_DELETE_FORWARD_ITEM:
                            result = ForwardTableManager.getInstance()
                                    .delete(body.getObj().getMacAddress());
                            if(result)
                                Logger.i("删除表项成功");
                            else
                                Logger.i("删除表项失败");
                            break;
                    }
                } catch (UnsupportedEncodingException e) {
                    Logger.e(e, e.getMessage());
                }
            }else {
                Logger.e("CRC校验错误，UDP传输过程中出错");
            }
        }
    }
}
