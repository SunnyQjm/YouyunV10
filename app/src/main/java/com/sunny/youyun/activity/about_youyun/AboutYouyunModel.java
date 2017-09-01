package com.sunny.youyun.activity.about_youyun;

/**
 * Created by Sunny on 2017/8/31 0031.
 */

class AboutYouyunModel implements AboutYouyunContract.Model{
    private AboutYouyunPresenter mPresenter;
    AboutYouyunModel(AboutYouyunPresenter aboutYouyunPresenter) {
        mPresenter = aboutYouyunPresenter;
    }
}
