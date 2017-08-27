package com.sunny.youyun.wifidirect.activity.single_trans.trans;

import android.os.Handler;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.utils.UUIDUtil;
import com.sunny.youyun.wifidirect.event.FileTransEvent;
import com.sunny.youyun.wifidirect.manager.SingleTransManager;
import com.sunny.youyun.wifidirect.manager.WifiDirectManager;
import com.sunny.youyun.wifidirect.model.TransLocalFile;
import com.sunny.youyun.wifidirect.server.SingleTransMode;
import com.sunny.youyun.wifidirect.utils.TransRxBus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Sunny on 2017/8/11 0011.
 */

class TransModel implements TransContract.Model {
    private TransPresenter mPresenter;
    private final String uuid = UUIDUtil.getUUID();
    private final List<TransLocalFile> mList = new CopyOnWriteArrayList<>();
    private final Handler handler = new Handler();


    TransModel(TransPresenter transPresenter) {
        mPresenter = transPresenter;
    }

    @Override
    public void begin() {
        TransRxBus.getInstance()
                .subscribe(uuid, fileTransEvent -> {
                    System.out.println(fileTransEvent.getAlready());
                    if (fileTransEvent.isDone() || fileTransEvent.getType() == FileTransEvent.Type.BEGIN)
                        handler.post(() -> mPresenter.update());
                    if(mList.size() <= fileTransEvent.getPosition())
                        return;
                    handler.post(() -> mPresenter.updateItem(mList.size() - fileTransEvent.getPosition() - 1, mList.get(fileTransEvent.getPosition())));
                }, throwable -> Logger.e(throwable, "面对面快传进度监听错误"));
        WifiDirectManager.getInstance().startServer(new SingleTransMode(mList));
    }

    @Override
    public void exit() {
        TransRxBus.getInstance().unSubscribe(uuid);
    }

    @Override
    public void send(String[] paths) {
        for (String path : paths) {
            WifiDirectManager.getInstance()
                    .sendFile(path, SingleTransManager.getInstance().getTargetInfo().getIp(), mList);
        }
    }

    @Override
    public List<TransLocalFile> getData() {
        return mList;
    }

}
