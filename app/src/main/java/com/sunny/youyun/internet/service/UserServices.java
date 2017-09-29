package com.sunny.youyun.internet.service;


import com.sunny.youyun.activity.person_file_manager.item.BaseFileItem;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.data_item.ConcernItem;
import com.sunny.youyun.model.data_item.Dynamic;
import com.sunny.youyun.model.response_body.BaseResponseBody;
import com.sunny.youyun.model.response_body.LoginResponseBody;
import com.sunny.youyun.model.response_body.RegisterResponseBody;
import com.sunny.youyun.model.result.GetUserInfoResult;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 *
 * Created by Administrator on 2017/3/18 0018.
 */
public interface UserServices {

    /**
     * 发送短信验证码
     * @param body 包含手机号码
     * @return
     */
    @POST(ApiInfo.SEND_CODE)
    Observable<BaseResponseBody<String>> sendCode(@Body RequestBody body);

    /**
     * 登录
     * @param body
     * @return
     */
    @POST(ApiInfo.LOGIN_URL)
    Observable<LoginResponseBody> login(@Body RequestBody body);


    /**
     * QQ登录
     * @param body
     * @return
     */
    @POST(ApiInfo.QQ_LOGIN_URL)
    Observable<LoginResponseBody> qqLogin(@Body RequestBody body);

    /**
     * 注册
     * @param body
     * @return
     */
    @POST(ApiInfo.REGISTER_URL)
    Observable<RegisterResponseBody> register(@Body RequestBody body);

    /**
     * 获取用户信息
     * @return
     */
    @GET(ApiInfo.GET_USER_INFO)
    Observable<BaseResponseBody<GetUserInfoResult>> getUserIno();

    /**
     * 修改头像
     * @param avatar
     * @return
     */
    @Multipart
    @POST(ApiInfo.MODIFY_AVATAR)
    Observable<BaseResponseBody<String>> modifyAvatar(@Part MultipartBody.Part avatar);

    /**
     * 登出
     * @return
     */
    @GET(ApiInfo.LOGOUT)
    Observable<BaseResponseBody<String>> logout();

    /**
     * 获取用户动态
     * @param page
     * @param size
     * @return
     */
    @GET(ApiInfo.GET_USER_DYNAMIC_URL)
    Observable<BaseResponseBody<Dynamic[]>> getUserDynamic(
            @Query(ApiInfo.GET_USER_DYNAMIC_PAGE) int page,
            @Query(ApiInfo.GET_USER_DYNAMIC_SIZE) int size
    );

    /**
     * 根据文件类型获取用户的文件
     * @param MIME
     * @param page
     * @param size
     * @return
     */
    @GET(ApiInfo.GET_USER_FILE_BY_TYPE_URL)
    Observable<BaseResponseBody<BaseFileItem[]>> getUserFileByType(
            @Query(ApiInfo.GET_USER_FILE_BY_TYPE_MIME) String MIME,
            @Query(ApiInfo.GET_USER_FILE_BY_TYPE_PAGE) int page,
            @Query(ApiInfo.GET_USER_FILE_BY_TYPE_SIZE) int size
    );

    /**
     * 获取关注的人列表
     * @return
     */
    @GET(ApiInfo.GET_FOLLOWING_LIST_URL)
    Observable<BaseResponseBody<ConcernItem[]>> getFollowingList(
            @Query(ApiInfo.GET_FOLLOWING_LIST_PAGE) int page,
            @Query(ApiInfo.GET_FOLLOWING_LIST_SIZE) int size
    );
}
