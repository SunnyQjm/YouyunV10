package com.sunny.youyun;

import com.sunny.youyun.internet.api.ApiInfo;

/**
 * Created by Sunny on 2017/10/12 0012.
 */

public class YouyunResultDeal {

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
