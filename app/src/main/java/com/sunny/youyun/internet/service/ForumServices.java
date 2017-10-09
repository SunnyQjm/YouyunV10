package com.sunny.youyun.internet.service;

import com.sunny.youyun.model.data_item.SearchData;
import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.InternetFile;
import com.sunny.youyun.model.data_item.Comment;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by root on 17-9-17.
 */

public interface ForumServices {
    /**
     * 获取社区文件
     * @param page
     * @param size
     * @param sortByDate
     * @param sortByDownloadCount
     * @return
     */
    @GET(ApiInfo.GET_FORUM_URL)
    Observable<BaseResponseBody<InternetFile[]>> getForumAll(
            @Query(ApiInfo.GET_FORUM_PAGE) int page, @Query(ApiInfo.GET_FORUM_SIZE) int size,
            @Query(ApiInfo.GET_FORUM_SORT_BY_DATE) boolean sortByDate,
            @Query(ApiInfo.GET_FORUM_SORT_BY_DOWNLOAD_COUNT) boolean sortByDownloadCount);

    /**
     * 添加评论
     * @param requestBody
     * @return
     */
    @PUT(ApiInfo.ADD_COMMENT_URL)
    Observable<BaseResponseBody<String>> addComment(
            @Body RequestBody requestBody
    );

    /**
     * 获取评论
     * @param fileId
     * @return
     */
    @GET(ApiInfo.GET_COMMENTS_URL)
    Observable<BaseResponseBody<Comment[]>> getComments(
            @Query(ApiInfo.GET_COMMENTS_FILE_ID) int fileId
    );

    /**
     * 点赞
     * @param fileId
     * @return
     */
    @GET(ApiInfo.STAR_URL)
    Observable<BaseResponseBody<String>> star(
            @Query(ApiInfo.STAR_FILE_ID) int fileId
    );

    @GET(ApiInfo.SEARCH_URL)
    Observable<BaseResponseBody<SearchData>> search(
            @Query(ApiInfo.SEARCH_STR) String str
    );
}
