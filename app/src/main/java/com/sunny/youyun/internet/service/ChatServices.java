package com.sunny.youyun.internet.service;


import com.sunny.youyun.activity.chat.item.MessageItemOther;
import com.sunny.youyun.fragment.main.message_fragment.item.PrivateLetterItem;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.data_item.Message;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Sunny on 2017/10/7 0007.
 */

public interface ChatServices {

    /**
     * 获取与单个人的聊天记录
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GET(ApiInfo.GET_CHAT_RECORD_URL)
    Observable<BaseResponseBody<Message[]>> getChatRecordSingle(
            @Query(ApiInfo.GET_CHAT_RECORD_USER_ID) int userId,
            @Query(ApiInfo.GET_CHAT_RECORD_PAGE) int page,
            @Query(ApiInfo.GET_CHAT_RECORD_SIZE) int size
    );

    /**
     * 获取一个群组的聊天记录
     * @param joinCode
     * @param page
     * @param size
     * @return
     */
    @GET(ApiInfo.GET_CHAT_RECORD_URL)
    Observable<BaseResponseBody<MessageItemOther>> getChatRecordGroup(
            @Query(ApiInfo.GET_CHAT_RECORD_JOIN_CODE) int joinCode,
            @Query(ApiInfo.GET_CHAT_RECORD_PAGE) int page,
            @Query(ApiInfo.GET_CHAT_RECORD_SIZE) int size
    );

    /**
     * 私信给单个用户，或一个群组
     * @param body
     * @return
     */
    @POST(ApiInfo.SEND_MESSAGE_URL)
    Observable<BaseResponseBody> sendMessage(@Body RequestBody body);

    /**
     * 获取私信列表
     * @return
     */
    @GET(ApiInfo.GET_PRIVATE_LETTER_LIST_URL)
    Observable<BaseResponseBody<PrivateLetterItem[]>> getPrivateLetterList(
            @Query(ApiInfo.GET_PRIVATE_LETTER_LIST_PAGE) int page,
            @Query(ApiInfo.GET_PRIVATE_LETTER_LIST_SIZE) int size
    );
}
