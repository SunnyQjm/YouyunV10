package com.sunny.youyun.activity.comment;

import android.os.Bundle;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.comment.adapter.CommentRecordAdapter;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.views.EasyBar;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

@Router(IntentRouter.CommentRecordActivity)
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
        adapter = new CommentRecordAdapter(mPresenter.getDatas());
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(this,
                RecyclerViewDividerItem.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        loadData(true);
    }

    @Override
    protected CommentRecordPresenter onCreatePresenter() {
        return new CommentRecordPresenter(this);
    }

    @Override
    protected void loadData(boolean isRefresh) {
        mPresenter.getCommentRecord(page, isRefresh);
    }

    @Override
    public void getCommentRecordSuccess() {
        updateAll();
    }
}
