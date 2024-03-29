package com.sunny.youyun.activity.chat;

import com.orhanobut.logger.Logger;
import com.sunny.youyun.internet.rx.RxObserver;
import com.sunny.youyun.internet.rx.RxResultHelper;
import com.sunny.youyun.internet.rx.RxSchedulersHelper;
import com.sunny.youyun.model.YouyunResultDeal;
import com.sunny.youyun.activity.chat.item.DateItem;
import com.sunny.youyun.activity.chat.item.MessageItemMy;
import com.sunny.youyun.activity.chat.item.MessageItemOther;
import com.sunny.youyun.base.entity.MultiItemEntity;
import com.sunny.youyun.internet.api.APIManager;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.YouyunExceptionDeal;
import com.sunny.youyun.model.data_item.Message;
import com.sunny.youyun.model.manager.UserInfoManager;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private long lastMessageTime = 0;

    ChatModel(ChatPresenter chatPresenter) {
        mPresenter = chatPresenter;
    }

    @Override
    public void getMessages(int userId, long times, int size, boolean isRefresh) {
        APIManager.getInstance()
                .getChatServices(GsonConverterFactory.create())
                .getChatRecordSingle(userId, times, size)
                .map(baseResponseBody -> {
                    if (baseResponseBody.isSuccess() &&
                            baseResponseBody.getData() != null) {
                        if (isRefresh)
                            mList.clear();
                        addAll(baseResponseBody.getData());
                        if (baseResponseBody.getData().length < ApiInfo.GET_DEFAULT_SIZE) {
                            return ApiInfo.RESULT_DEAL_TYPE_LOAD_FINISH;
                        }
                        return ApiInfo.RESULT_DEAL_TYPE_SUCCESS;
                    } else {
                        return ApiInfo.RESULT_DEAL_TYPE_FAIL;
                    }
                })
                .compose(RxSchedulersHelper.INSTANCE.io_main())
                .subscribe(new RxObserver<Integer>(mPresenter) {
                    @Override
                    public void _onNext(Integer integer) {
                        YouyunResultDeal.INSTANCE.deal(integer, new YouyunResultDeal.OnResultListener() {
                            @Override
                            public void onSuccess() {
                                mPresenter.getMessagesSuccess();
                            }

                            @Override
                            public void onLoadFinish() {
                                mPresenter.allDataLoadFinish();
                            }

                            @Override
                            public void onFail() {
                                Logger.e("获取聊天记录失败");
                            }
                        });
                    }
                });
    }

    private void addAll(Message[] data) {
        for (Message d : data) {
            //第一次添加
            if (mList.size() == 0) {
                lastMessageTime = d.getCreateTime();
            }
            System.out.println(d.getCreateTime() - lastMessageTime);
            //间隔五分钟放时间item
            if (lastMessageTime - d.getCreateTime() > 1000 * 60 * 5) {
                mList.add(new DateItem.Builder()
                        .date(lastMessageTime)
                        .build());
            }
            if (d.getUser().getId() == UserInfoManager.getInstance().getUserId()) {
                mList.add(new MessageItemMy(d));
            } else {
                mList.add(new MessageItemOther(d));
            }
            lastMessageTime = d.getCreateTime();
        }
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
                .compose(RxSchedulersHelper.INSTANCE.io_main())
                .subscribe(new RxObserver<BaseResponseBody>(mPresenter) {
                    @Override
                    public void _onNext(BaseResponseBody baseResponseBody) {
                        if (baseResponseBody.isSuccess())
                            mPresenter.sendMessageSuccess(content);
                    }
                });
    }
}
