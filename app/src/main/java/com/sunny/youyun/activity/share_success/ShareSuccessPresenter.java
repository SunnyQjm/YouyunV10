package com.sunny.youyun.activity.share_success;

import java.io.IOException;

/**
 * Created by Sunny on 2017/10/22 0022.
 */

class ShareSuccessPresenter extends ShareSuccessContract.Presenter{
    ShareSuccessPresenter(ShareSuccessActivity shareSuccessActivity) {
        mView = shareSuccessActivity;
        mModel = new ShareSuccessModel(this);
    }

    @Override
    protected void start() throws IOException {

    }


}
