package com.sunny.youyun;

import com.sunny.youyun.internet.api.ApiInfo;
import com.sunny.youyun.model.response_body.BaseResponseBody;

import java.util.Collections;
import java.util.List;

/**
 * Created by Sunny on 2017/10/12 0012.
 */

public class YouyunResultDeal {

    public static <T> int dealData(BaseResponseBody<T[]> responseBody, List<T> mList,
                                    boolean isRefresh){
        if(responseBody.isSuccess() &&
                responseBody.getData() != null){
            if(isRefresh)
                mList.clear();
            Collections.addAll(mList, responseBody.getData());
            if(responseBody.getData().length < ApiInfo.GET_DEFAULT_SIZE){
                return ApiInfo.RESULT_DEAL_TYPE_LOAD_FINISH;
            }
            return ApiInfo.RESULT_DEAL_TYPE_SUCCESS;
        } else {
            return ApiInfo.RESULT_DEAL_TYPE_FAIL;
        }
    }

    /**
     * 处理数据处理结果
     * @param code 处理结果标识
     * @param listener 回调
     */
    public static void deal(int code, OnResultListener listener){
        switch (code){
            case ApiInfo.RESULT_DEAL_TYPE_FAIL:
                if(listener != null)
                    listener.onFail();
                break;
            case ApiInfo.RESULT_DEAL_TYPE_LOAD_FINISH:
                if(listener != null)
                    listener.onLoadFinish();
                break;
            case ApiInfo.RESULT_DEAL_TYPE_SUCCESS:
                if(listener != null)
                    listener.onSuccess();
                break;
        }
    }



    public interface OnResultListener{
        void onSuccess();
        void onLoadFinish();
        void onFail();
    }
}
