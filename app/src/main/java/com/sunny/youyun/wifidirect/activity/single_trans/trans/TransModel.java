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
                    Logger.i(fileTransEvent.toString());
                    switch (fileTransEvent.getType()){
                        case FINISH:
                        case BEGIN:
                            handler.post(()-> mPresenter.update());
                            break;
                        case UPLOAD:
                        case DOWNLOAD:
                            System.out.println(fileTransEvent.getAlready());
                            if (fileTransEvent.isDone() || fileTransEvent.getType() == FileTransEvent.Type.BEGIN)
                                handler.post(() -> mPresenter.update());
                            if(mList.size() <= fileTransEvent.getPosition()) {
                                Logger.i("mList.size() > fileTransEvent.getPosition()");
                                return;
                            }
                            handler.post(() -> mPresenter.updateItem(mList.size() - fileTransEvent.getPosition() - 1, mList.get(fileTransEvent.getPosition())));
                            break;
                        case ERROR:
                            mPresenter.showError("传输失败");
                            break;
                    }
                }, throwable -> Logger.e(throwable, "面对面快传进度监听错误"));
        SingleTransMode.getInstance()
                .bindMList(mList);
        WifiDirectManager.getInstance().startServer(SingleTransMode.getInstance());
    }

    @Override
    public void exit() {
        TransRxBus.getInstance().unSubscribe(uuid);
    }

    @Override
    public void send(String[] paths) {
        System.out.println("ip: " + SingleTransManager.getInstance().getTargetInfo().getIp());
        System.out.println(SingleTransManager.getInstance().getTargetInfo());
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
