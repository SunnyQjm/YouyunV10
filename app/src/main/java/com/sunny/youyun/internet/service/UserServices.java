package com.sunny.youyun.internet.service;


import com.sunny.youyun.model.response_body.BaseResponseBody;
import com.sunny.youyun.model.response_body.LoginResponseBody;
import com.sunny.youyun.model.response_body.RegisterResponseBody;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
    @POST("sendCode")
    Observable<BaseResponseBody<String>> sendCode(@Body RequestBody body);

    @POST("login")
    Observable<LoginResponseBody> login(@Body RequestBody body);
//
//    @FormUrlEncoded
//    @POST(ApiInfo.LOGIN_URL)
//    Observable<User> login(
//            @Field(ApiInfo.LOGIN_USERNAME) String username,
//            @Field(ApiInfo.LOGIN_PASSWORD) String password
//    );

    @POST("register")
    Observable<RegisterResponseBody> register(@Body RequestBody body);
//
//    @POST("login")
//    Observable<LoginResponseBody> login(@Body RequestBody body);
//
//    @POST("logout")
//    String logut();
//
//    @Multipart
//    @POST("user/setAvatar")
//    Observable<ModifyAvatarResponseBody> modifyAvatar(@Part MultipartBody.Part avatar);
//
//    @POST("user/update")
//    Observable<StringResponseBody> modifyInfo(@Body RequestBody body);
//
//    @POST("bookstore/updateDescription")
//    Observable<UpdateDescriptionResponseBody> updateDescription(@Body RequestBody body);
//
//    @POST("bookstore/updateName")
//    Observable<UpdateNameResponseBody> updateName(@Body RequestBody body);
//
    @POST("logout")
    Observable<String> logout();
}
