package com.sunny.youyun.activity.about_youyun;

import java.io.IOException;

/**
 * Created by Sunny on 2017/8/31 0031.
 */

class AboutYouyunPresenter extends AboutYouyunContract.Presenter{
    AboutYouyunPresenter(AboutYouyunActivity aboutYouyunActivity) {
        mView = aboutYouyunActivity;
        mModel = new AboutYouyunModel(this);
    }

    @Override
    protected void start() throws IOException {

    }
}
