package com.sunny.youyun.activity.comment;

import android.os.Bundle;
import android.view.View;

import com.sunny.youyun.R;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.views.EasyBar;

public class CommentRecordActivity extends BaseRecyclerViewActivity<CommentRecordPresenter>
        implements CommentRecordContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        easyBar.setTitle(getString(R.string.comment_record));
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
    protected void onRefreshBegin() {

    }

    @Override
    protected void OnRefreshBeginSync() {

    }

    @Override
    protected void OnRefreshFinish() {

    }

    @Override
    protected void onLoadBeginSync() {

    }

    @Override
    protected void onLoadFinish() {

    }

    @Override
    protected CommentRecordPresenter onCreatePresenter() {
        return new CommentRecordPresenter(this);
    }
}
