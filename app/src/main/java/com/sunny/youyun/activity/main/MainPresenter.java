package com.sunny.youyun.activity.main;

import com.sunny.youyun.R;
import com.sunny.youyun.mvp.BaseView;

/**
 * Created by Sunny on 2017/6/24 0024.
 */

public class MainPresenter extends MainContract.Presenter{
    private static final int[] res1 = {R.drawable.icon_mainpage, R.drawable.icon_mainpage_selected};
    private static final int[] res2 = {R.drawable.icon_findng, R.drawable.icon_finding_selected};
    private static final int[] res3 = {R.drawable.icon_message, R.drawable.icon_message_selected};
    private static final int[] res4 = {R.drawable.icon_mine, R.drawable.icon_mine_selected};
    private static int[] pos = {1, 0, 0, 0};
    public MainPresenter(BaseView baseView) {
        mView = (MainContract.View) baseView;
        mModel = new MainModel(this);
    }

    @Override
    protected void start() {

    }

    @Override
    void changeBottomImg(int position) {
        for (int i = 0; i < pos.length; i++) {
            if(i == position)
                pos[i] = 1;
            else
                pos[i] = 0;
        }
        mView.changeBottomImg(res1[pos[0]], res2[pos[1]], res3[pos[2]], res4[pos[3]]);
    }
}
