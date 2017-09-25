package com.sunny.youyun.internet.service;

import com.sunny.youyun.activity.person_file_manager_index.item.FileItem;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.response_body.BaseResponseBody;

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
    @POST(ApiInfo.UPLOAD_FILE_URL)
//    Observable<BaseResponseBody<String>> uploadFile(@PartMap Map<String, RequestBody> paramMap, @Part() List<MultipartBody.Part> files);
    Observable<BaseResponseBody<String>> uploadFile(@PartMap Map<String, RequestBody> paramMap, @Part MultipartBody.Part file);

    @POST(ApiInfo.UPLOAD_FILE_CHECK_URL)
    Observable<BaseResponseBody<String>> checkMd5(@Body RequestBody body);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);

    @GET(ApiInfo.GET_FILE_INFO)
    Observable<BaseResponseBody<InternetFile>> getFileInfo(
            @Query(value = ApiInfo.GET_FILE_INFO_IDENTIFY_CODE) String code);

    @GET(ApiInfo.GET_UPLOAD_FILES_URL)
    Observable<BaseResponseBody<FileItem[]>> getUploadFiles(
            @Query(ApiInfo.GET_UPLOAD_FILES_PARENT_ID) String parentId
    );

    @GET(ApiInfo.GET_UPLOAD_FILES_URL)
    Observable<BaseResponseBody<FileItem[]>> getUploadFiles();

    @POST(ApiInfo.CREATE_DIRECTORY_URL)
    Observable<BaseResponseBody<FileItem[]>> createDirectory(@Body RequestBody body);
}
