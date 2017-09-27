package com.sunny.youyun.internet.service;

import com.sunny.youyun.model.response_body.LoginTokenResponseBody;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Sunny on 2017/9/21 0021.
 */

public interface TokenServices {

    /**
     * 利用token获取cookie
     * @param requestBody
     * @return
     */
    @POST("updateToken")
    Call<LoginTokenResponseBody> updateCookieByToken(
            @Body RequestBody requestBody
    );
}
