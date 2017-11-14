package com.sunny.youyun.activity.comment;

import android.os.Bundle;
import android.view.View;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.R;
import com.sunny.youyun.activity.comment.adapter.CommentRecordAdapter;
import com.sunny.youyun.base.RecyclerViewDividerItem;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;
import com.sunny.youyun.base.adapter.BaseQuickAdapter;
import com.sunny.youyun.model.data_item.CommentRecord;
import com.sunny.youyun.utils.RouterUtils;
import com.sunny.youyun.views.EasyBar;

/**
 * Created by Sunny on 2017/10/13 0013.
 */

@Router(IntentRouter.CommentRecordActivity)
public class CommentRecordActivity extends BaseRecyclerViewActivity<CommentRecordPresenter>
        implements CommentRecordContract.View, BaseQuickAdapter.OnItemClickListener {

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
        adapter = new CommentRecordAdapter(getMPresenter().getDatas());
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(this,
                RecyclerViewDividerItem.VERTICAL));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.recycler_empty_view);
        adapter.setOnItemClickListener(this);
        loadData(true);
    }

    @Override
    protected CommentRecordPresenter onCreatePresenter() {
        return new CommentRecordPresenter(this);
    }

    @Override
    protected void loadData(boolean isRefresh) {
        getMPresenter().getCommentRecord(page, isRefresh);
    }

    @Override
    public void getCommentRecordSuccess() {
        updateAll();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CommentRecord commentRecord = (CommentRecord) adapter.getItem(position);
        if(commentRecord == null || commentRecord.getFile() == null)
            return;
        RouterUtils.openToFileDetailOnline(this, commentRecord.getFileId(),
                commentRecord.getFile().getIdentifyCode());
    }
}
