package com.sunny.youyun.internet.service;


import com.sunny.youyun.activity.person_file_manager.item.BaseFileItem;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.User;
import com.sunny.youyun.model.data_item.Collection;
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

    /**
     * 关注别人
     * @param otherId
     * @return
     */
    @GET(ApiInfo.CONCERN_OTHER_USER_URL)
    Observable<BaseResponseBody> concern(
            @Query(ApiInfo.CONCERN_OTHER_USER_OTHER_ID) int otherId
    );

    /**
     * 获取其他用户的信息
     * @param otherId
     * @return
     */
    @GET(ApiInfo.GET_OTHER_USER_INFO_URL)
    Observable<BaseResponseBody<User>> getOtherUserInfo(
            @Query(ApiInfo.GET_OTHER_USER_INFO_OTHER_ID) int otherId
    );

    /**
     * 修改用户的基本信息
     * @param body
     * @return
     */
    @POST(ApiInfo.MODIFY_USER_INFO_URL)
    Observable<BaseResponseBody<User>> modifyUserInfo(@Body RequestBody body);


    /**
     * 获取用户动态
     * @param page
     * @param size
     * @return
     */
    @GET(ApiInfo.GET_USER_DYNAMIC_URL)
    Observable<BaseResponseBody<Collection[]>> getUserCollections(
            @Query(ApiInfo.GET_USER_DYNAMIC_PAGE) int page,
            @Query(ApiInfo.GET_USER_DYNAMIC_SIZE) int size,
            @Query(ApiInfo.GET_USER_DYNAMIC_TYPE) int type
    );

    @GET(ApiInfo.GET_OTHER_USER_PUBLIC_FILES_URL)
    Observable<BaseResponseBody<InternetFile[]>> getOtherPublicFiles(
            @Query(ApiInfo.GET_OTHER_USER_PUBLIC_FILES_USER_ID) int userId,
            @Query(ApiInfo.GET_OTHER_USER_PUBLIC_FILES_PAGE) int page,
            @Query(ApiInfo.GET_OTHER_USER_PUBLIC_FILES_SIZE) int size
    );

}
