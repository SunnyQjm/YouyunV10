package com.sunny.youyun.activity.star;

import android.os.Bundle;
import android.view.View;

import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.views.EasyBar;

public class StarRecordActivity extends BaseRecyclerViewActivity<StarRecordPresenter>
        implements StarRecordContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void loadData(boolean isRefresh) {

    }

    private void init() {
        easyBar.setTitle(getString(R.string.star_record));
        easyBar.setOnEasyBarClickListener(new EasyBar.OnEasyBarClickListener() {
            @Override
            public void onLeftIconClick(View view) {
                onBackPressed();
            }

            @Override
            public void onRightIconClick(View view) {

            }
        });

    }

    @Override
    protected StarRecordPresenter onCreatePresenter() {
        return new StarRecordPresenter(this);
    }
}
