package com.sunny.youyun.internet.service;


import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.Dynamic;
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

    @POST(ApiInfo.LOGIN_URL)
    Observable<LoginResponseBody> login(@Body RequestBody body);


    @POST(ApiInfo.QQ_LOGIN_URL)
    Observable<LoginResponseBody> qqLogin(@Body RequestBody body);

    @POST(ApiInfo.REGISTER_URL)
    Observable<RegisterResponseBody> register(@Body RequestBody body);

    @GET(ApiInfo.GET_USER_INFO)
    Observable<BaseResponseBody<GetUserInfoResult>> getUserIno();

    @Multipart
    @POST(ApiInfo.MODIFY_AVATAR)
    Observable<BaseResponseBody<String>> modifyAvatar(@Part MultipartBody.Part avatar);

    @GET(ApiInfo.LOGOUT)
    Observable<BaseResponseBody<String>> logout();

    @GET(ApiInfo.GET_USER_DYNAMIC_URL)
    Observable<BaseResponseBody<Dynamic[]>> getUserDynamic(
            @Query(ApiInfo.GET_USER_DYNAMIC_PAGE) int page,
            @Query(ApiInfo.GET_USER_DYNAMIC_SIZE) int size
    );
}
