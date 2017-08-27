package com.sunny.youyun.internet.service;

import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Sunny on 2017/6/22 0022.
 */

public interface FileServices {
    @Multipart
    @POST("file/upload")
    Observable<BaseResponseBody<String>> uploadFile(@PartMap Map<String, RequestBody> paramMap, @Part() List<MultipartBody.Part> files);

    @POST("file/uploadCheck")
    Observable<BaseResponseBody<String>> checkMd5(@Body RequestBody body);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);

    @GET(ApiInfo.GET_FILE_INFO)
    Observable<BaseResponseBody<InternetFile[]>> getFileInfo(
            @Query(value = ApiInfo.GET_FILE_INFO_IDENTIFY_CODE) String code);
//    @GET
//    Observable<BaseResponseBody<InternetFile[]>> getFileInfo(
//            @Url String url
//    );
}
