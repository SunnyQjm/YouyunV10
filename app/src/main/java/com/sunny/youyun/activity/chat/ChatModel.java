package com.sunny.youyun.activity.chat;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.activity.chat.item.MessageItem;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunny on 2017/10/4 0004.
 */

class ChatModel implements ChatContract.Model {
    private final ChatPresenter mPresenter;
    private final List<MultiItemEntity> mList = new ArrayList<>();

    ChatModel(ChatPresenter chatPresenter) {
        mPresenter = chatPresenter;
    }

    @Override
    public void getMessages(int userId, int page, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getChatServices(GsonConverterFactory.create())
                .getChatRecordSingle(userId, page, size)
                .subscribe(new Observer<BaseResponseBody<MessageItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody<MessageItem> messageItemBaseResponseBody) {
                        if (messageItemBaseResponseBody.isSuccess() &&
                                messageItemBaseResponseBody.getData() != null) {
                            if(isRefresh)
                                mList.clear();
                            Collections.addAll(mList, messageItemBaseResponseBody.getData());
                            mPresenter.getMessagesSuccess();
                        } else {
                            mPresenter.showTip("没有获取到信息");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("获取聊天记录失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public List<MultiItemEntity> getData() {
        return mList;
    }

    @Override
    public void sendMessage(int userId, String content) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ApiInfo.SEND_MESSAGE_USER_ID, userId);
            jsonObject.put(ApiInfo.SEND_MESSAGE_CONTENT, content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse(ApiInfo.MEDIA_TYPE_JSON),
                jsonObject.toString());
        APIManager.getInstance()
                .getChatServices(GsonConverterFactory.create())
                .sendMessage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPresenter.addSubscription(d);
                    }

                    @Override
                    public void onNext(BaseResponseBody baseResponseBody) {
                        if(baseResponseBody.isSuccess())
                            mPresenter.sendMessageSuccess(content);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("发送消息失败", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
