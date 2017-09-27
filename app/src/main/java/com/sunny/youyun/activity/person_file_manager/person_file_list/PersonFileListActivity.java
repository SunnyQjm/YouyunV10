package com.sunny.youyun.activity.person_file_manager.person_file_list;

import android.os.Bundle;

import com.github.mzule.activityrouter.annotation.Router;
import com.sunny.youyun.IntentRouter;
import com.sunny.youyun.activity.person_file_manager.adapter.FileAdapter;
import com.sunny.youyun.activity.person_file_manager.config.DisplayTypeConfig;
import com.sunny.youyun.base.activity.BaseRecyclerViewActivity;

@Router(value = {IntentRouter.PersonFileListActivity + "/:type"},
        intParams = "type", stringParams = "parentId")
public class PersonFileListActivity extends BaseRecyclerViewActivity<PersonFileListPresenter> implements PersonFileListContract.View {

    private FileAdapter adapter;
    private int page = 1;
    private int type = DisplayTypeConfig.TYPE_DIVIDE_APPLICATION;
    private String parentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        adapter = new FileAdapter(mPresenter.getData());
        adapter.bindToRecyclerView(recyclerView);
        type = getIntent().getIntExtra("type", DisplayTypeConfig.TYPE_DIVIDE_APPLICATION);
        parentId = getIntent().getStringExtra("parentId");
        if(type > 10){
            refreshLayout.setLoadAble(false);
            mPresenter.getFileByPath(parentId);
        } else {
            mPresenter.getFileByType(DisplayTypeConfig.getMIMEByType(type), page, true);
        }
    }

    @Override
    protected void onRefreshBegin() {
        page = 1;
    }

    @Override
    protected void OnRefreshBeginSync() {
        if(type < 10){
            mPresenter.getFileByType(DisplayTypeConfig.getMIMEByType(type), page, true);
        } else {
            mPresenter.getFileByPath(parentId);
        }
    }

    @Override
    protected void OnRefreshFinish() {

    }

    @Override
    protected void onLoadBeginSync() {
        page++;
        mPresenter.getFileByType(DisplayTypeConfig.getMIMEByType(type), page, true);
    }

    @Override
    protected void onLoadFinish() {

    }

    @Override
    protected PersonFileListPresenter onCreatePresenter() {
        return new PersonFileListPresenter(this);
    }

    @Override
    public void getFileByTypeSuccess() {
        updateUI();
    }


    @Override
    public void getFileByPathSuccess() {
        updateUI();
    }

    private void updateUI(){
        if(adapter != null)
            adapter.notifyDataSetChanged();
    }
}
